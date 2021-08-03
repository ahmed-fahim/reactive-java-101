const timeUtils = require('./timeUtils');
const fetch = require('node-fetch')

const basePath = "http://0.0.0.0:8080/";
const blockingFutureUrl = basePath + "info-blocking-future";
const completableFutureUrl = basePath + "info-completable-future";
const elasticSchedulerUrl = basePath + "info-scheduler-elastic";
const boundedElasticSchedulerUrl = basePath + "info-scheduler-bounded-elastic";
const executorServiceSchedulerUrl = basePath + "info-scheduler-executor-service";

let RandomInformationAttributes = [
	"information",
	"requestTime",
	"responseTime",
	"errorString"
];

let httpStatusCounter = [];
function recordStatus(status) {
	if(status in httpStatusCounter) {
		httpStatusCounter[status] = httpStatusCounter[status] + 1;
	}
	else{
		httpStatusCounter[status] = 1;
	}
}

let totalResponseCounter = 0;
function printIfFinishedAllFetch(lastSerial) {
	totalResponseCounter++;
	if (totalResponseCounter === lastSerial) {
		httpStatusCounter.forEach((statusCount, status) => {
			console.log("HTTPStatus " + status + " : " + statusCount);
		})
	}
}

function handleRandomInformation(responseStatus, randomInformation) {
	recordStatus(responseStatus);
	let receivedInformation = "";
	let firstAttribute = true;
	let responseDuration = null;
	RandomInformationAttributes.forEach((attribute) => {
		if (attribute in randomInformation) {
			receivedInformation = receivedInformation + (!firstAttribute ? ";\t" : "") + attribute + " : " + randomInformation[attribute];
			if (firstAttribute) {
				firstAttribute = false;
			}
		}
	});
	if ("requestTime" in randomInformation
		&& "responseTime" in randomInformation) {
		responseDuration = timeUtils.timeDifference(randomInformation["requestTime"], randomInformation["responseTime"]);
	}
	console.log("[HTTP %s] %s", responseStatus, receivedInformation);
	if (responseDuration !== null) {
		console.log("Response-Duration : %s\n", responseDuration);
	}
}

const getRequestParams = () => {
	return {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: timeUtils.currentDateTime()
	};
}



function fetchRandomInformation(url, lastSerial) {
	fetch(url, getRequestParams())
		.then(response => response.json()
			.then(data => ({
					status: response.status,
					body: data
				})
			)
			.then(jsonResp => handleRandomInformation(jsonResp.status, jsonResp.body))
			.catch(reason => console.log("[Err-Caught] " + reason + "\n"))
		)
		.catch(reason => {
			recordStatus(500);
			console.log("[Fetch-Err-Caught] " + reason + "\n");
		})
		.finally(() => printIfFinishedAllFetch(lastSerial));
}

function testJavaAPI(url, count) {
	for (let i = 1; i <= count; i++) {
		fetchRandomInformation(url, count);
	}
}

const blockingFutureTest = (count) => testJavaAPI(blockingFutureUrl, count);
const completableFutureTest = (count) => testJavaAPI(completableFutureUrl, count);
const elasticSchedulerTest = (count) => testJavaAPI(elasticSchedulerUrl, count);
const boundedElasticSchedulerTest = (count) => testJavaAPI(boundedElasticSchedulerUrl, count);
const executorServiceSchedulerTest = (count) => testJavaAPI(executorServiceSchedulerUrl, count);

module.exports = {
	blockingFutureTest : blockingFutureTest,
	completableFutureTest : completableFutureTest,
	elasticSchedulerTest : elasticSchedulerTest,
	boundedElasticSchedulerTest : boundedElasticSchedulerTest,
	executorServiceSchedulerTest : executorServiceSchedulerTest
}