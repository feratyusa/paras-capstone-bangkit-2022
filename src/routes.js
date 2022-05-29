const Joi = require("joi");
const {
  predictPhotoHandler,
  createUserHandler,
  loginHandler,
  getUserByUsername,
  getHistoryHandler,
  predictTestPhotoHandler,
  editUserByUsernameHandler,
  getHistoryByIdHandler,
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
          handphone: Joi.string().regex(/^[0-9]*$/),
          email: Joi.string().regex(/[@]{1}/),
          photo: Joi.any(),
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
  {
    method: "POST",
    path: "/test-predict-photo",
    options: {
      auth: false,
      payload: {
        output: "file",
        allow: "multipart/form-data",
        multipart: true,
      },
      handler: predictTestPhotoHandler,
    },
  },
];

module.exports = routes;
