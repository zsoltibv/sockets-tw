let ws;
const DISCONNECT = "Disconnect";
const CONNECT = "Connect";
let username;

function connect() {
    username = document.getElementById("username").value;
    let host = document.location.host;

    /* ws://localhost:8080/chat/user */
    ws = new WebSocket("ws://" + host + "/chat/" + username);
    console.log(ws)
    ws.onmessage = function (event) {
        let log = document.getElementById("log");
        let message = JSON.parse(event.data);

        switch (message.type) {
            case "message_to_group":
                console.log(message)
                log.innerHTML += `${message.from} (${message.to}) : ${message.content}\n`;
                break;
            case "view_all_users":
                let users = message.content.split(", ");
                console.log(users)
                bindUsersToDropdown(users);
                break;
            case "view_all_groups":
                let groups = message.content.split(", ");
                console.log(groups)
                bindGroupsToDropdown(groups);
                break;
            default:
                console.log("Unknown message type: " + message.type);
        }
    }
}

function send() {
    let selectedGroup = document.getElementById("groupDropdownToSendTo").value;

    let content = document.getElementById("msg").value;
    let json = JSON.stringify({
        "content": content,
        "to": selectedGroup,
        "from": username,
        "type": "message_to_group"
    });

    ws.send(json);

    /* Clear massage input area */
    document.getElementById("msg").value = "";
}

function disconnect() {
    let json = JSON.stringify({
        "content": "disconnect",
        "type": "disconnect"
    });

    ws.send(json)
}

function connectionCall() {
    let content = document.getElementById("conBtn").textContent;

    if (content === DISCONNECT) {
        /* Change button's text to Connect */
        document.getElementById("conBtn").textContent = CONNECT;
        /* Enable write function & erase the old username */
        document.getElementById("username").disabled = false;
        document.getElementById("username").value = "";
        this.disconnect();
    } else {
        /* Change button's text to Disconnected and disable the field */
        document.getElementById("conBtn").textContent = DISCONNECT;
        document.getElementById("username").disabled = true;
        this.connect();
    }
}

function createGroup() {
    let groupName = document.getElementById("groupName").value.trim();
    if (groupName !== "") {
        let json = JSON.stringify({
            "content": groupName,
            "type": "create_group"
        });

        ws.send(json);
    }
}

function getAllUsers() {
    let json = JSON.stringify({
        "from": username,
        "type": "view_all_users"
    });

    ws.send(json);
}

function getAllGroups() {
    let json = JSON.stringify({
        "from": username,
        "type": "view_all_groups"
    });

    ws.send(json);
}

function bindUsersToDropdown(users) {
    let dropdown = document.getElementById("userDropdown");
    dropdown.innerHTML = ""; // Clear existing options

    // Create and append options to the dropdown
    users.forEach(function (user) {
        let option = document.createElement("option");
        option.value = user;
        option.text = user;
        dropdown.add(option);
    });
}

function bindGroupsToDropdown(groups) {
    let dropdown = document.getElementById("groupDropdown");
    dropdown.innerHTML = ""; // Clear existing options

    // Create and append options to the dropdown
    groups.forEach(function (group) {
        let option = document.createElement("option");
        option.value = group;
        option.text = group;
        dropdown.add(option);
    });

    let dropdown2 = document.getElementById("groupDropdownToSendTo");
    dropdown2.innerHTML = ""; // Clear existing options

    // Create and append options to the dropdown
    groups.forEach(function (user) {
        let option = document.createElement("option");
        option.value = user;
        option.text = user;
        dropdown2.add(option);
    });
}

function addToGroup(){
    let selectedUser = document.getElementById("userDropdown").value;
    let selectedGroup = document.getElementById("groupDropdown").value;

    let json = JSON.stringify({
        "to": selectedGroup,
        "content": selectedUser,
        "type": "add_to_group",
    });

    ws.send(json);
}