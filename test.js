const bcrypt = require("bcrypt");

const pass = "butahuruf";
const hash = bcrypt.hashSync(pass, 10);
bcrypt.compare("buta", hash).then((result) => console.log(result));
