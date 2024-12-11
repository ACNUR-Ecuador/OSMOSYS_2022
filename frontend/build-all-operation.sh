node ./build/post-build-operation.js cri
node ./build/post-build-operation.js ecu
node ./build/post-build-operation.js mex
node ./build/post-build-operation.js per
node ./build/post-build-operation.js slv
node ./build/post-build-operation.js test
node ./build/post-build-operation.js ven

zip -r dist.zip dist
