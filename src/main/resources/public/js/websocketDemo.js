$(function () {
	connect();
});

var total = {};

function connect() {
	
	reset();
	
	// Establish the WebSocket connection and set up event handlers
	var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");

	webSocket.onmessage = function(msg) {
		updateMonitor(msg.data);
	};

	webSocket.onclose = function(e) {
		console.log( 'Socket is closed. Reconnect will be attempted in 5 second.', e.reason);
		setTimeout(function() {
			connect();
		}, 5000);
	};

	webSocket.onerror = function(err) {
		console.error('Socket encountered error: ', err.message, 'Closing socket');
		webSocket.close();
	};
}

function reset() {
	$("#topics").empty();
	for (let [key, value] of Object.entries(total)) {
		total[key] = 0;
	}
}

function updateMonitor(msg) {
	raw = JSON.parse(msg);
	message = '<a class="list-group-item list-group-item-action">';
	message += '<div class="d-flex w-100 justify-content-between">';
	message += '  <div class="topic mb-1">' + raw.topic + '</div>';
	message += '  <div class="ts">' + getDate(raw.ts) + '</div>';
	message += '</div>';
	message += '<p class="mb-1">' + decodeMessage(raw.message) + '</p>';
	message += '</a>';
	
	updateSummary(raw.topic);
	$("#topics").prepend(message);
}

function updateSummary(topic) {
	nb = total[topic];
	if (nb == undefined) {
		$("#summary").append('<li class="nav-item"><a class="nav-link" href="#">' + topic + ' <span class="badge badge-info" data-topic="' + topic + '">0</span></a></li>');
		nb = 0;
	}
	nb++;
	total[topic] = nb;
	$("#summary").find("[data-topic='" + topic + "']").text(nb);
	return nb;
}

function decodeMessage(jsonString) {
	try {
		data = JSON.parse(jsonString);
		return '<div class="origin"><span class="field">origine:</span> ' + data.origine + '</div>'
				+ '<div class="message"><span class="field">message:</span> ' + highlight(prettyJSON(data.data)) + '</div>';
	} catch (error) {
		return jsonString;
	}
}

function prettyJSON(jsonString) {
	try {
		return JSON.stringify(JSON.parse(jsonString),null,2);
	} catch(error) {
		return jsonString;
	}
}

function getDate(ts) {
	var currentDate = new Date(ts);

	var date = currentDate.getDate();
	var month = currentDate.getMonth()+1;
	var year = currentDate.getFullYear();
	var hour = currentDate.getHours();
	var min = currentDate.getMinutes();
	var sec = currentDate.getSeconds();
	var ms = currentDate.getMilliseconds();
	return pad(date) + "/" + pad(month) + "/" + year + " " + pad(hour) + ":" + pad(min) + ":" + pad(sec) + "." + pad3(ms);
}

function pad(n) {
    return n<10 ? '0'+n : n;
}
function pad3(n) {
	return n<10 ? '00'+n : (n<100 ? '0'+n : n);
}