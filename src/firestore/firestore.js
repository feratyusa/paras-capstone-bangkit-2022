const Firestore = require("@google-cloud/firestore");

const db = new Firestore({
  projectId: "leafy-tractor-349708",
});

const HISTORIES = "histories";
const USERS = "users";

const usersCollection = db.collection(USERS);
const historiesCollection = db.collection(HISTORIES);

/**
 * Users Field
 * username, password, nama, email, no hp, foto
 */

/**
 * History Fields
 * id, gejala, tanggal
 */

/**
 * Product Fields
 * Nama, Detail, Kategori, Gambar
 */

module.exports = { usersCollection, historiesCollection };
