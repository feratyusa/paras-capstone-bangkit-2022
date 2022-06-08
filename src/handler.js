const bcrypt = require("bcrypt");
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

  const authorization = Buffer.from(`${username}:${password}`).toString("base64");

  const response = h.response({
    status: "Success",
    credentials: {
      username,
      authorization,
    },
  });
  response.code(200);
  return response;
}

async function loginAsGuestHandler(request, h) {
  const { date } = request.payload; // YYYY-MM-DD

  const guestRef = usersCollection.doc();
  const username = `g_${guestRef.id}`;
  const password = `g_${Math.round(Math.random() * 100000000)}`;
  const email = `${username}@email.com`;
  const handphone = "";
  const destination = "default/default-photo-profile.jpg";

  await guestRef.set({
    username,
    password,
    email,
    handphone,
    photo: `https://storage.googleapis.com/349708_profile/${destination}`,
    date,
  });

  const authorization = Buffer.from(`${username}:${password}`).toString("base64");

  const response = h.response({
    status: "Success",
    credentials: {
      username,
      password,
      authorization,
    },
  });
  response.code(201);
  return response;
}

async function createUserHandler(request, h) {
  const { username, password, email, handphone, photo } = request.payload;
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

  let destination = "default/default-photo-profile.jpg";
  if (photo !== null) {
    /**
     * Save profile photo to bucket
     */
    destination = `${username}/${username}-photo-profile.jpg`;
    await uploadFile(photo.path, destination, "profile");
  }

  const newUser = {
    username,
    password: bcrypt.hashSync(password, 10),
    email,
    handphone,
    photo: `https://storage.googleapis.com/349708_profile/${destination}`,
    historyCount: 0,
  };

  await usersCollection.doc(username).set(newUser);
  const authorization = Buffer.from(`${username}:${password}`).toString("base64");
  const response = h.response({
    status: "Success",
    credentials: {
      username,
      authorization,
    },
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

  if (username !== request.auth.credentials.username) {
    const response = h.response({
      status: "Forbidden",
      message: "You are not allowed to view this page",
    });
    response.code(403);
    return response;
  }

  const response = h.response(user.data());
  response.code(200);
  return response;
}

async function editUserByUsernameHandler(request, h) {
  const data = request.payload;
  const { username } = request.params;

  if (username !== request.auth.credentials.username) {
    const response = h.response({
      status: "Forbidden",
      message: "You are not allowed to view this page",
    });
    response.code(403);
    return response;
  }
  const userRef = usersCollection.doc(username);
  if (data.email) {
    await userRef.update({ email: data.email });
  } else if (data.handphone) {
    await userRef.update({ handphone: data.handphone });
  } else if (data.photo) {
    /**
     * Edit photo profile on storage
     */
    const destination = `${username}/${username}-photo-profile.jpg`;
    await uploadFile(data.photo.path, destination, "profile");

    await userRef.update({ photo: `https://storage.googleapis.com/349708_profile/${destination}` });
  } else if (data.password) {
    const passHash = bcrypt.hashSync(data.password);

    await userRef.update({ password: passHash });
  } else {
    const response = h.response({
      status: "Failed",
      message: "Edit failed because no new data provided",
    });
    response.code(400);
    return response;
  }

  const user = await userRef.get();
  const response = h.response({
    status: "Success",
    data: user.data(),
  });
  response.code(202);
  return response;
}

async function getHistoryHandler(request, h) {
  const { username } = request.params;

  if (username !== request.auth.credentials.username) {
    const response = h.response({
      status: "Forbidden",
      message: "You are not allowed to view this page",
    });
    response.code(403);
    return response;
  }

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

async function getHistoryByIdHandler(request, h) {
  const { username, id } = request.params;

  if (username !== request.auth.credentials.username) {
    const response = h.response({
      status: "Forbidden",
      message: "You are not allowed to view this page",
    });
    response.code(403);
    return response;
  }

  const historyRef = usersCollection.doc(username).collection("histories").doc(id);
  const history = await historyRef.get();
  if (!history.exists) {
    const response = h.response({
      status: "Not Found",
      message: "History you are looking for doesn't exist",
    });
    response.code(404);
    return response;
  }

  const response = h.response({
    status: "Success",
    data: history.data(),
  });
  response.code(200);
  return response;
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

  /**
   * Predict users image
   */
  const prediction = await runPredict(image.path);
  // console.log(prediction);

  /**
   * Save the prediction history image
   */
  // Get count history of a user
  const userRef = usersCollection.doc(request.auth.credentials.username);
  const user = await userRef.get();
  const count = parseInt(user.data().historyCount, 10) + 1; // Plus one

  // Save the image
  let extension = ".jpg";
  if (imageHeader["content-type"] !== "image/jpeg") {
    extension = ".jpeg";
  } else if (imageHeader["content-type"] !== "image/png") {
    extension = ".png";
  }
  const destination = `${request.auth.credentials.username}/history${count}${extension}`;
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
    symptom: prediction,
    date: `${year}-${month}-${date}`,
  };
  const res = await addHistory(request.auth.credentials.username, historyData);

  const response = h.response(res);
  response.code(200);
  return response;
}

module.exports = {
  loginHandler,
  createUserHandler,
  predictPhotoHandler,
  getUserByUsername,
  getHistoryHandler,
  editUserByUsernameHandler,
  getHistoryByIdHandler,
  loginAsGuestHandler,
};
