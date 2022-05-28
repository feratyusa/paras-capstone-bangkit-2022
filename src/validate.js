const bcrypt = require("bcrypt");
const { usersCollection } = require("./firestore/firestore");

const validate = async (request, username, password) => {
  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  if (!user.exists) {
    return { credentials: null, isValid: false };
  }

  const isValid = await bcrypt.compare(password, user.data().password);
  const credentials = { id: user.id, name: user.name };

  return { isValid, credentials };
};

module.exports = validate;
