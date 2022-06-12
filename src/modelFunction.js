const fs = require("fs");

const tf = require("@tensorflow/tfjs");
const tfNode = require("@tensorflow/tfjs-node");

function sobel_edges(image) {
  image_shape = image.shape;
  kernels = [
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
  num_kernels = kernels.length;
  kernels = tf.transpose(kernels, [1, 2, 0]);
  kernels = tf.expandDims(kernels, -2);
  kernels_tf = tf.cast(kernels, "float32");

  kernels_tf = tf.tile(kernels_tf, [1, 1, 3, 1]);

  pad_sizes = [
    [0, 0],
    [1, 1],
    [1, 1],
    [0, 0],
  ];
  padded = tf.mirrorPad(image, pad_sizes, "reflect");
  strides = [1, 1, 1, 1];
  output = tf.depthwiseConv2d(padded, kernels_tf, strides, "valid");
  shape = tf.concat([image_shape, [num_kernels]], 0);
  output = tf.reshape(output, shape.arraySync());

  return output;
}

function get_array_data(array, idx) {
  customSlice4 = (array) => array.slice(idx, idx + 1);
  customSlice3 = (array) => array.map(customSlice4);
  customSlice2 = (array) => array.map(customSlice3);
  customSlice1 = (array) => array.map(customSlice2);

  result = array.map(customSlice1);

  return result;
}

async function runPredict(imagePath) {
  const MODEL_URL =
    "https://storage.googleapis.com/paras-model/model/model.json";

  const model = await tf.loadGraphModel(MODEL_URL);

  const channel = 3;
  const imageSize = [150, 150];
  const label = ["Acne Level 0", "Acne Level 1", "Acne Level 2", "Normal"];

  let image = fs.readFileSync(imagePath);
  image = tfNode.node.decodeImage(image, channel);
  image = tf.image.resizeBilinear(image, imageSize);
  image = image.expandDims();

  sobel = sobel_edges(image);
  sobel = sobel.arraySync();
  sobel_y = get_array_data(sobel, 0);
  sobel_x = get_array_data(sobel, 1);

  gradient_magnitude = tf.sqrt(tf.add(tf.square(sobel_y), tf.square(sobel_x)));
  normalization_matrix = tf.mul(
    tf.cast(tf.div(gradient_magnitude, 255.0), "int32"),
    255.0
  );
  gradient_magnitude = tf.sub(gradient_magnitude, normalization_matrix);
  gradient_magnitude = tf.add(tf.div(gradient_magnitude, 4), 0.5);

  img_edge = tf.reshape(gradient_magnitude, image.shape);

  const prediction = model.predict([image, img_edge]);
  const index = prediction.argMax(1).arraySync()[0];
  const facialDisease = label[index];

  return facialDisease;
}

module.exports = runPredict;
