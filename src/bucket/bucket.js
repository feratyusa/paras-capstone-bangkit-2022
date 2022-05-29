const { Storage } = require("@google-cloud/storage");

let storage = "";
if (process.env.NODE_ENV !== "production") {
  storage = new Storage({
    projectId: "leafy-tractor-349708",
    keyFilename: "../keys/leafy-tractor-349708-199269b05dcb.json",
  });
} else {
  storage = new Storage({
    projectId: "leafy-tractor-349708",
  });
}

const PROFILE_BUCKET_NAME = "349708_profile";
const HISTORY_BUCKET_NAME = "349708_history";
const historyBucket = storage.bucket(HISTORY_BUCKET_NAME);
const profileBucket = storage.bucket(PROFILE_BUCKET_NAME);

async function uploadFile(filePath, destination, flag) {
  if (flag === "history") await historyBucket.upload(filePath, { destination });
  else if (flag === "profile") await profileBucket.upload(filePath, { destination });
}

module.exports = { uploadFile, PROFILE_BUCKET_NAME, HISTORY_BUCKET_NAME };
