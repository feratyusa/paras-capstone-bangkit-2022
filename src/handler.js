const fs = require("fs");
const bcrypt = require("bcrypt");
const { users } = require("./firestore/firestore");

async function loginHandler(request, h) {
  const { email, password } = request.payload;
  const userRef = users.doc(email);
  const user = await userRef.get();
  if (!user.exists) {
    const response = h.response({
      status: "Failed login",
      message: "Email or Password is wrong",
      dev: "Email doesn't exist",
      em: email,
    });
    response.code(409);
    return response;
  }

  const match = await bcrypt.compare(password, user.data().password);
  if (!match) {
    const response = h.response({
      status: "Failed login",
      message: "Email or Password is wrong",
      dev: "Password is wrong",
    });
    response.code(409);
    return response;
  }

  const response = h.response({
    status: "Login success",
    message: user.email,
  });
  response.code(200);
  return response;
}

function createUserHandler(request, h) {
  const { email, password } = request.payload;
  const userRef = users.doc(email);
  return userRef.get().then((user) => {
    if (user.exists) {
      const response = h.response({
        status: "Failed creating user",
        message: "Email already exists",
      });
      response.code(409);
      return response;
    }

    const newUser = {
      password: bcrypt.hashSync(password, 10),
    };

    return users
      .doc(email)
      .set(newUser)
      .then(() => {
        const response = h.response({
          status: "User creation success",
          message: email,
        });
        response.code(201);
        return response;
      })
      .catch(() => {
        const response = h.response({
          status: "User creation failed",
          message: "Error when creating new user on server",
        });
        response.code(500);
        return response;
      });
  });
}

function predictPhotoHandler(request, h) {
  const { image } = request.payload;

  const imageHeader = image.headers;
  if (
    imageHeader["content-type"] !== "image/jpg" &&
    imageHeader["content-type"] !== "image/png" &&
    imageHeader["content-type"] !== "image/jpeg"
  ) {
    const response = h.response({
      status: "Failed",
      message: "Server can not process file format. Try uploading a JPG, JPEG, or PNG file.",
    });
    response.code(406);
    return response;
  }

  const imageData = fs.readFileSync(image.path, "base64");

  const response = h.response({
    status: "Success",
    data: imageData,
  });
  response.code(200);
  return response;
}

module.exports = {
  loginHandler,
  createUserHandler,
  predictPhotoHandler,
};
