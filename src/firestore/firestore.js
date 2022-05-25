const Firestore = require("@google-cloud/firestore");

const db = new Firestore({
  projectId: "leafy-tractor-349708",
});

const HISTORIES = "histories";
const USERS = "users";

const users = db.collection(USERS);
const histories = db.collection(HISTORIES);

module.exports = { db, users, histories };
