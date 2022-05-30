const fs = require("fs");

const tf = require("@tensorflow/tfjs");
const tfNode = require("@tensorflow/tfjs-node");

async function runPredict(imagePath) {
  const MODEL_URL =
    "https://storage.googleapis.com/paras-model/model/model.json";

  const model = await tf.loadGraphModel(MODEL_URL);

  const channel = 3;
  const imageSize = [150, 150]; // Image size 150x150
  const label = ["acne level 0", "acne level 1", "acne level 2", "normal"];

  let image = fs.readFileSync(imagePath);
  image = tfNode.node.decodeImage(image, channel);
  image = tf.image.resizeBilinear(image, imageSize);
  image = image.expandDims();

  const prediction = model.predict(image);
  const index = prediction.argMax(1).arraySync()[0];
  const facialDisease = label[index];

  return facialDisease;
}

module.exports = runPredict;
