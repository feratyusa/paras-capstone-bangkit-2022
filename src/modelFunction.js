const fs = require("fs");

const tf = require("@tensorflow/tfjs");
const tfNode = require("@tensorflow/tfjs-node");

function sobelEdges(image) {
  const imageShape = image.shape;
  let kernels = [
    [
      [-1, -2, -1],
      [0, 0, 0],
      [1, 2, 1],
    ],
    [
      [-1, 0, 1],
      [-2, 0, 2],
      [-1, 0, 1],
    ],
  ];
  const numKernels = kernels.length;
  kernels = tf.transpose(kernels, [1, 2, 0]);
  kernels = tf.expandDims(kernels, -2);
  let kernelsTf = tf.cast(kernels, "float32");

  kernelsTf = tf.tile(kernelsTf, [1, 1, 3, 1]);

  const padSizes = [
    [0, 0],
    [1, 1],
    [1, 1],
    [0, 0],
  ];
  const padded = tf.mirrorPad(image, padSizes, "reflect");
  const strides = [1, 1, 1, 1];
  let output = tf.depthwiseConv2d(padded, kernelsTf, strides, "valid");
  const shape = tf.concat([imageShape, [numKernels]], 0);
  output = tf.reshape(output, shape.arraySync());

  return output;
}

function getArrayData(array, idx) {
  const customSlice4 = (arrays) => arrays.slice(idx, idx + 1);
  const customSlice3 = (arrays) => arrays.map(customSlice4);
  const customSlice2 = (arrays) => arrays.map(customSlice3);
  const customSlice1 = (arrays) => arrays.map(customSlice2);

  const result = array.map(customSlice1);

  return result;
}

async function runPredict(imagePath) {
  const MODEL_URL = "https://storage.googleapis.com/paras-model/model/model.json";

  const model = await tf.loadGraphModel(MODEL_URL);

  const channel = 3;
  const imageSize = [150, 150];
  const label = ["Acne Level 0", "Acne Level 1", "Acne Level 2", "Normal"];

  let image = fs.readFileSync(imagePath);
  image = tfNode.node.decodeImage(image, channel);
  image = tf.image.resizeBilinear(image, imageSize);
  image = image.expandDims();

  let sobel = sobelEdges(image);
  sobel = sobel.arraySync();
  const sobelY = getArrayData(sobel, 0);
  const sobelX = getArrayData(sobel, 1);

  let gradientMagnitude = tf.sqrt(tf.add(tf.square(sobelY), tf.square(sobelX)));
  const normalizationMatrix = tf.mul(tf.cast(tf.div(gradientMagnitude, 255.0), "int32"), 255.0);
  gradientMagnitude = tf.sub(gradientMagnitude, normalizationMatrix);
  gradientMagnitude = tf.add(tf.div(gradientMagnitude, 4), 0.5);

  const imgEdge = tf.reshape(gradientMagnitude, image.shape);

  const prediction = model.predict([image, imgEdge]);
  const index = prediction.argMax(1).arraySync()[0];
  const facialDisease = label[index];

  return facialDisease;
}

module.exports = runPredict;
