const Hapi = require("@hapi/hapi");
const routes = require("./routes");
const { validate } = require("./validate");

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
