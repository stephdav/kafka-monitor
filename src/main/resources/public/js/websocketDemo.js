connect();

function connect() {
	// Establish the WebSocket connection and set up event handlers
	var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");

//	webSocket.onopen = function(msg) {
//		// Nothing to do
//	};

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

function updateMonitor(msg) {
	raw = JSON.parse(msg);
	message = '<a class="list-group-item list-group-item-action">';
	message += '<div class="d-flex w-100 justify-content-between">';
	message += '  <h5 class="mb-1">' + raw.topic + '</h5>';
	message += '  <small>' + getDate(raw.ts) + '</small>';
	message += '</div>';
	message += '<p class="mb-1">' + raw.message + '</p>';
	message += '</a>';

	insert("topics", message);
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

function prettyJSON(jsonString) {
	try {
		return JSON.stringify(JSON.parse(jsonString),null,2);
	} catch(error) {
		return jsonString;
	}
}

// Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
	id(targetId).insertAdjacentHTML("afterbegin", message);
}

// Helper function for selecting element by id
function id(id) {
	return document.getElementById(id);
}