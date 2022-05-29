const bcrypt = require("bcrypt");
const { usersCollection } = require("./firestore/firestore");
const runPredict = require("./modelFunction");

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
  const { username } = request.payload;

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

  const historyRef = userRef.collection(HISTORIES);
  /**
   * Check if history collection exists on a user or
   * if a user has not make a submission before
   *  */
  const checkHistoryCollection = await historyRef.limit(1).get();
  if (!checkHistoryCollection.exists) {
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
      description: doc.data().description,
      date: doc.data().date,
    });
  });
}

async function predictPhotoHandler(request, h) {
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

  const prediction = await runPredict(image.path);

  /**
   * Save the prediction history
   */
  

  const response = h.response(prediction);
  response.code(200);
  return response;
}

module.exports = {
  loginHandler,
  createUserHandler,
  predictPhotoHandler,
  getUserByUsername,
};
