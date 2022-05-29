const Firestore = require("@google-cloud/firestore");

let db = "";
if (process.env.NODE_ENV !== "production") {
  db = new Firestore({
    projectId: "leafy-tractor-349708",
    keyFilename: "../keys/leafy-tractor-349708-199269b05dcb.json",
  });
} else {
  db = new Firestore({
    projectId: "leafy-tractor-349708",
  });
}

const USERS = "users";

const usersCollection = db.collection(USERS);

async function addHistory(username, data) {
  const historyRef = usersCollection.doc(username).collection("histories").doc();
  const ID = { id: historyRef.id };
  const fullData = { ...data, ...ID };
  await historyRef.set(fullData);

  return fullData;
}

/**
 * Users Field
 * username, password, nama, email, no hp, foto
 */

/**
 * History Fields
 * id, symptom, description, date
 */

/**
 * Product Fields
 * Nama, Detail, Kategori, Gambar
 */

module.exports = { usersCollection, addHistory };
