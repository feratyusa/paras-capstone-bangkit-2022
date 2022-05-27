const fs = require("fs");
const bcrypt = require("bcrypt");
const { usersCollection } = require("./firestore/firestore");

async function loginHandler(request, h) {
  const { username, password } = request.payload;
  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  if (!user.exists) {
    const response = h.response({
      status: "Failed login",
      message: "Username or Password is wrong",
      dev: "Username doesn't exist",
      em: username,
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
    message: user.data().username,
  });
  response.code(200);
  return response;
}

function createUserHandler(request, h) {
  const { username, password, email, handphone = "", photo = "" } = request.payload;
  const userRef = usersCollection.doc(username);
  return userRef.get().then((user) => {
    if (user.exists) {
      const response = h.response({
        status: "Failed creating user",
        message: "Username already exists",
      });
      response.code(409);
      return response;
    }

    const newUser = {
      username,
      password: bcrypt.hashSync(password, 10),
      email,
      handphone,
      photo,
    };

    return usersCollection
      .doc(username)
      .set(newUser)
      .then(() => {
        const response = h.response({
          status: "User creation success",
          message: username,
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

function getUserByUsername(request, h){
  
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
