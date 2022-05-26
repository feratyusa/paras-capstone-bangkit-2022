const bcrypt = require("bcrypt");

const text = "butahuruf";
const textC = bcrypt.hashSync(text, 10);
const textD = "butahuruf";
const match = bcrypt.compareSync(textD, textC);

console.log(match);
