const moment = require('moment');
const fetch = require('node-fetch')

const reactiveUrl = "http://0.0.0.0:8080/fetch-new-information"

const currentDateTime = () => {
	return moment().format("YYYY-MM-DD HH:mm:ss:SSS Z");

}


let RandomInformationAttributes = [
	"information",
	"requestTime",
	"responseTime",
	"errorString"
];


function handleRandomInformation(randomInformation) {
	let receivedInformation = "";
	let firstAttribute = true;
	RandomInformationAttributes.forEach((attribute) => {
		if(attribute in randomInformation) {
			receivedInformation = receivedInformation + (!firstAttribute ? ";\t" : "") + attribute + " : " + randomInformation[attribute];
			if(firstAttribute) {
				firstAttribute = false;
			}
		}
	});
	console.log("[Success] %s\n",receivedInformation);
}

function handleErrorResponse(errorResponse) {
	console.log("[Error] " + errorResponse);
}

const getRequestParams = () => {
	return {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: currentDateTime()
	};
}
let successCounter = 0;
let failureCounter = 0;

function printIfFinishedAllFetch(lastSerial) {
	if(successCounter + failureCounter === lastSerial) {
		console.log("Success = %d, Failure = %d\n", successCounter, failureCounter);
	}
}

function fetchRandomInformation(currentSerial, lastSerial) {
	fetch(reactiveUrl, getRequestParams())
		.then((response) => {
			console.log("response-status : " + response.status);
			return response.json()
		})
		.then(randomInformation => {
			handleRandomInformation(randomInformation);
			successCounter++;
		})
		.catch(reason => {
			handleErrorResponse(reason);
			failureCounter++;
		})
		.finally(() => printIfFinishedAllFetch(lastSerial));
}

function testJavaAPI(count) {
	for(let i = 1; i <= count; i++) {
		fetchRandomInformation(i, count);
	}
}

testJavaAPI(10000);