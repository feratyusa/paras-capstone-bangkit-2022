const bcrypt = require("bcrypt");
const fs = require("fs");
const { uploadFile } = require("./bucket/bucket");
const { usersCollection, addHistory } = require("./firestore/firestore");
const runPredict = require("./modelFunction");

async function loginHandler(request, h) {
  const { username, password } = request.payload;
  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  if (!user.exists) {
    const response = h.response({
      status: "Failed",
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
      status: "Failed",
      message: "Email or Password is wrong",
      dev: "Password is wrong",
    });
    response.code(409);
    return response;
  }

  const response = h.response({
    status: "Login",
    data: {
      username: user.data().username,
      passEncrypt: user.data().password,
    },
  });
  response.code(200);
  return response;
}

async function createUserHandler(request, h) {
  const { username, password, email, handphone = "", photo = "" } = request.payload;
  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  if (user.exists) {
    const response = h.response({
      status: "Failed",
      message: "Username already exists",
    });
    response.code(409);
    return response;
  }

  /**
   * Save profile photo to bucket
   */
  const destination = `${username}/${username}-photo-profile.jpg`;
  await uploadFile(photo.path, destination, "profile");

  const newUser = {
    username,
    password: bcrypt.hashSync(password, 10),
    email,
    handphone,
    photo: `https://storage.googleapis.com/349708_profile/${destination}`,
    historyCount: 0,
  };

  await usersCollection.doc(username).set(newUser);
  const response = h.response({
    status: "Success",
    data: newUser,
  });
  response.code(201);
  return response;
}

async function getUserByUsername(request, h) {
  const { username } = request.params;

  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  if (!user.exists) {
    const response = h.response({
      status: "Not Found",
      message: "Username not found",
    });
    response.code(404);
    return response;
  }

  const response = h.response(user.data());
  response.code(200);
  return response;
}

async function getHistoryHandler(request, h) {
  const { username } = request.params;

  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  if (!user.exists) {
    const response = h.response({
      status: "Not Found",
      message: "Username not found",
    });
    response.code(404);
    return response;
  }

  const historyRef = usersCollection.doc(username).collection("histories");
  /**
   * Check if history collection exists on a user or
   * if a user has not make a submission before
   *  */
  const checkHistoryCollection = await historyRef.limit(1).get();
  if (checkHistoryCollection.length === null) {
    const response = h.response({
      status: "Empty",
      message: "This user doesn't have any history yet",
    });
    response.code(200);
    return response;
  }

  // Get all history of a user
  const snapshotData = [];
  const snapshot = await historyRef.get();
  snapshot.forEach((doc) => {
    snapshotData.push({
      id: doc.id,
      symptom: doc.data().symptom,
      // description: doc.data().description,
      date: doc.data().date,
    });
  });
  const response = h.response(snapshotData);
  response.code(200);
  return response;
}

async function predictPhotoHandler(request, h) {
  const { username, image } = request.payload;

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

  // const prediction = await runPredict(image.path);
  // console.log(prediction);

  /**
   * Save the prediction history image
   */
  // Get count history of a user
  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  const count = parseInt(user.data().historyCount, 10) + 1; // Plus one

  // Save the image
  const destination = `${username}/history${count}`;
  uploadFile(image.path, destination, "history");

  // Update the user history count
  await userRef.update({ historyCount: count });

  /**
   * Save the history
   */
  const dateObject = new Date();
  const year = dateObject.getFullYear();
  const month = `0${dateObject.getMonth() + 1}`.slice(-2);
  const date = `0${dateObject.getDate() + 1}`.slice(-2);
  const historyData = {
    symptom: "null",
    date: `${year}-${month}-${date}`,
  };
  const res = await addHistory(username, historyData);

  const response = h.response(res);
  response.code(200);
  return response;
}

async function predictTestPhotoHandler(request, h) {
  // Request image payload
  const { image } = request.payload;

  // Take the local temp path of the image
  const imagePath = image.path;

  // Do something with the image
  const imageEncrypt = fs.readFileSync(imagePath, "base64");

  const response = h.response({ imageEncrypt });
  return h.response(response);
}

module.exports = {
  loginHandler,
  createUserHandler,
  predictPhotoHandler,
  getUserByUsername,
  getHistoryHandler,
  predictTestPhotoHandler,
};
