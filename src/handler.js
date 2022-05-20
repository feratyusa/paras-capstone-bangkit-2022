const fs = require("fs");
const imageToBase64 = require("image-to-base64");

function predictHandler(request, h) {
  const { image: imageURL } = request.payload;

  if (!imageURL) {
    const response = h.response({
      status: "image failed",
      message: "Image not found",
    });
    response.code(400);
    return response;
  }

  return imageToBase64(imageURL)
    .then((imageBase64) => {
      const response = h.response({
        status: "success",
        data: imageBase64,
      });
      response.code(200);
      return response;
    })
    .catch((error) => {
      const response = h.response({
        status: "fail",
        message: error,
      });
      response.code(400);
      return response;
    });
}

function predictPhotoHandler(request, h) {
  const data = request.payload;

  const imageData = fs.readFileSync(data.image.path, "base64");
  console.log(imageData);

  const response = h.response({
    status: "success",
    data: imageData,
  });
  response.code(200);
  return response;
}

module.exports = { predictHandler, predictPhotoHandler };
