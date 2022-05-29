const Firestore = require("@google-cloud/firestore");

const db = new Firestore({
  projectId: "leafy-tractor-349708",
  keyFilename: "../keys/leafy-tractor-349708-199269b05dcb.json",
});

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
