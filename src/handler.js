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

module.exports = predictHandler;
