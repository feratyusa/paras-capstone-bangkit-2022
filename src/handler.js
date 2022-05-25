const fs = require("fs");
const imageToBase64 = require("image-to-base64");
const bcrypt = require("bcrypt");
const { db, users } = require("./firestore/firestore");

async function createUser(request, h) {
  const { email, password } = request.payload;
  const userRef = users.doc(email);
  const user = await userRef.get();
  if (!user) {
    const response = h.response({
      status: "failed creating user",
      message: "email already exists",
    });
    response.code(402);
    return response;
  }

  const newUser = {
    password: bcrypt.hashSync(password, 10),
  };

  return db
    .collection("cities")
    .doc(email)
    .set(newUser)
    .then(() => {
      const response = h.response({
        status: "user creation success",
        message: email,
      });
      response.code(200);
      return response;
    });
}

function predictHandler(request, h) {
  const { image: imageURL } = request.payload;

  if (!imageURL) {
    const response = h.response({
      status: "image failed",
      message: "Image not found",
    });
    response.code(400);
    return response;
  }

  return imageToBase64(imageURL)
    .then((imageBase64) => {
      const response = h.response({
        status: "success",
        data: imageBase64,
      });
      response.code(200);
      return response;
    })
    .catch((error) => {
      const response = h.response({
        status: "fail",
        message: error,
      });
      response.code(400);
      return response;
    });
}

function predictPhotoHandler(request, h) {
  const data = request.payload;

  const imageData = fs.readFileSync(data.image.path, "base64");
  console.log(imageData);

  const response = h.response({
    status: "success",
    data: imageData,
  });
  response.code(200);
  return response;
}

module.exports = { createUser, predictHandler, predictPhotoHandler };
