const moment = require('moment');
const DATE_TIME_FORMAT = "YYYY-MM-DD HH:mm:ss:SSS Z";
const TIME_DIFFERENCE_FORMAT = "HH:mm:ss:SSS";
const momentTime = (timeStamp) => moment(timeStamp,DATE_TIME_FORMAT);

const currentDateTime = () => moment().format(DATE_TIME_FORMAT);

const timeDifference = (start, end) => {
	return moment.utc(momentTime(end).diff(momentTime(start))).format(TIME_DIFFERENCE_FORMAT);
}

module.exports = {
	currentDateTime : currentDateTime,
	timeDifference : timeDifference
}