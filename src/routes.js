const Joi = require("joi");
const {
  predictPhotoHandler,
  createUserHandler,
  loginHandler,
  getUserByUsername,
  getHistoryHandler,
  editUserByUsernameHandler,
  getHistoryByIdHandler,
  loginAsGuestHandler,
} = require("./handler");

const routes = [
  {
    method: "POST",
    path: "/login",
    options: {
      auth: false,
      validate: {
        payload: Joi.object({
          username: Joi.string().alphanum().min(4).max(50).required(),
          password: Joi.string().alphanum().min(8).max(30).required(),
        }),
      },
      payload: {
        allow: ["multipart/form-data", "application/json"],
        multipart: true,
      },
      handler: loginHandler,
    },
  },
  {
    method: "POST",
    path: "/register",
    options: {
      auth: false,
      validate: {
        payload: Joi.object({
          username: Joi.string().alphanum().min(4).max(50).required(),
          password: Joi.string().alphanum().min(8).max(30).required(),
          handphone: Joi.string()
            .regex(/^[0-9]*$/)
            .allow("")
            .default(null),
          email: Joi.string()
            .regex(/[@]{1}/)
            .required(),
          photo: Joi.any().allow("").optional().default(null),
        }),
      },
      payload: {
        output: "file",
        allow: "multipart/form-data",
        multipart: true,
      },
      handler: createUserHandler,
    },
  },
  {
    method: "POST",
    path: "/loginAsGuest",
    options: {
      validate: {
        payload: Joi.object({
          date: Joi.date().default(new Date().toISOString().slice(0, 10)),
        }),
      },
      auth: false,
      payload: {
        output: "file",
        allow: "multipart/form-data",
        multipart: true,
      },
      handler: loginAsGuestHandler,
    },
  },
  {
    method: "GET",
    path: "/account/{username}",
    handler: getUserByUsername,
  },
  {
    method: "POST",
    path: "/account/{username}/edit",
    options: {
      validate: {
        payload: Joi.object({
          password: Joi.string().alphanum().min(8).max(30),
          email: Joi.string().regex(/[@]{1}/),
          handphone: Joi.string().regex(/^[0-9]*$/),
          photo: Joi.any(),
        }),
      },
      payload: {
        output: "file",
        allow: "multipart/form-data",
        multipart: true,
      },
      handler: editUserByUsernameHandler,
    },
  },
  {
    method: "GET",
    path: "/history/{username}",
    handler: getHistoryHandler,
  },
  {
    method: "GET",
    path: "/history/{username}/{id}",
    handler: getHistoryByIdHandler,
  },
  {
    method: "POST",
    path: "/predict-photo",
    options: {
      payload: {
        output: "file",
        allow: "multipart/form-data",
        multipart: true,
      },
      handler: predictPhotoHandler,
    },
  },
];

module.exports = routes;
