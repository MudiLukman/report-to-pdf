function connect() {
	var wsocket;
	wsocket = new WebSocket("ws://localhost:8080/download"); 
	wsocket.onopen = onopen;
	wsocket.onmessage = onmessage;
	wsocket.onclose = onclose;
}

function onopen() {
	console.log("Connected!");
}

function onmessage(event) {
	const fileJson = JSON.parse(event.data);
	const file = {
		fileName: fileJson.fileName,
		data: fileJson.binaryData
	};
	const blob = toBlob(file.data);
	const blobUrl = window.URL.createObjectURL(blob);
	const downloadLink = document.createElement("a");
	document.body.appendChild(downloadLink);
	downloadLink.href = blobUrl;
	downloadLink.setAttribute("download", file.fileName);
	downloadLink.click();
	console.log("File Downloaded!");
	downloadLink.parentNode.removeChild(downloadLink);
}

function onclose(e) {
	console.log("Connection closed.");
}

function toBlob(base64Data) {
	const byteCharacters = atob(base64Data);
	const byteNumbers = new Array(byteCharacters.length);
	for (let i = 0; i < byteCharacters.length; i++) {
		byteNumbers[i] = byteCharacters.charCodeAt(i);
	}
	const byteArray = new Uint8Array(byteNumbers);
	return new Blob([byteArray], {type: ""});
}

window.addEventListener("load", connect, false);