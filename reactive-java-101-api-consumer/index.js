const apiTestUtils = require('./utils/apiTestUtils');
const argv = require('yargs/yargs')(process.argv.slice(2)).argv;

const blockingFutureTest = apiTestUtils.blockingFutureTest;
const completableFutureTest = apiTestUtils.completableFutureTest;
const elasticSchedulerTest = apiTestUtils.elasticSchedulerTest;
const boundedElasticSchedulerTest = apiTestUtils.boundedElasticSchedulerTest;
const executorServiceSchedulerTest = apiTestUtils.executorServiceSchedulerTest;

let argCount = 3;
if (argv.count !== null && argv.count !== undefined) {
	argCount = argv.count
}

executorServiceSchedulerTest(argCount);