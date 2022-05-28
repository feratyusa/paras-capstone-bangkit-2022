const Hapi = require("@hapi/hapi");
const bcrypt = require("bcrypt");
const routes = require("./routes");
const { usersCollection } = require("./firestore/firestore");

const validate = async (request, username, password) => {
  const userRef = usersCollection.doc(username);
  const user = await userRef.get();
  if (!user.exists) {
    return { credentials: null, isValid: false };
  }

  const isValid = await bcrypt.compare(password, user.data().password);
  const credentials = { username: user.data().username };

  return { isValid, credentials };
};

const init = async () => {
  const server = Hapi.server({
    port: parseInt(process.env.PORT, 10) || 8080,
    host: process.env.NODE_ENV !== "production" ? "localhost" : "0.0.0.0",
    routes: {
      cors: {
        origin: ["*"],
      },
    },
  });

  await server.register(require("@hapi/basic"));

  server.auth.strategy("simple", "basic", { validate });

  server.auth.default("simple");

  server.route(routes);
  await server.start();
  console.log(`Server berjalan pada ${server.info.uri}.`);
};

init();
