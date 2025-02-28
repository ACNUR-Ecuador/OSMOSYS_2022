const path = require('path');
const fs = require('fs');
const util = require('util');

// get application version from package.json
const appVersion = require('../package.json').version;

// promisify core API's
const readDir = util.promisify(fs.readdir);
const writeFile = util.promisify(fs.writeFile);
const readFile = util.promisify(fs.readFile);

console.log('\nRunning post-build tasks');

// Get the directory from the command line arguments
const args = process.argv.slice(2);
if (args.length === 0) {
  console.error('Error: Please provide the target directory as an argument.');
  process.exit(1);
}
const targetDir = args[0]; // e.g., "dist/test"

// Validate the provided path
const versionFilePath = path.join(__dirname, '/../dist/', targetDir, '/version.json');
let mainHash = '';
let mainBundleFile = '';

// RegExp to find main.bundle.js, even if it doesn't include a hash in its name (dev build)
let mainBundleRegexp = /^main.?([a-z0-9]*)?.js$/;

// Read the target folder and find the file
readDir(path.join(__dirname, '/../dist/', targetDir, '/'))
  .then(files => {
    mainBundleFile = files.find(f => mainBundleRegexp.test(f));
    if (mainBundleFile) {
      let matchHash = mainBundleFile.match(mainBundleRegexp);
      // If it has a hash in its name, mark it down
      if (matchHash.length > 1 && !!matchHash[1]) {
        mainHash = matchHash[1];
      }
    }
    console.log(`Writing version and hash to ${versionFilePath}`);
    // Write current version and hash into the version.json file
    mainHash = new Date().getTime();
    const src = `{"version": "${appVersion}", "hash": "${mainHash}"}`;
    console.log('version');
    console.log(src);
    return writeFile(versionFilePath, src);
  })
  .then(() => {
    // Main bundle file not found, dev build?
    if (!mainBundleFile) {
      return;
    }
    console.log(`Replacing hash in the ${mainBundleFile}`);
    // Replace hash placeholder in our main.js file so the code knows its current hash
    const mainFilepath = path.join(__dirname, '/../dist/', targetDir, '/', mainBundleFile);
    return readFile(mainFilepath, 'utf8').then(mainFileData => {
      const replacedFile = mainFileData.replace('{{POST_BUILD_ENTERS_HASH_HERE}}', mainHash);
      return writeFile(mainFilepath, replacedFile);
    });
  })
  .catch(err => {
    console.error('Error with post-build:', err);
  });
