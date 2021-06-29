(function (window, document, undefined) {
    /**
     * represent the id of the last message shown.
     * @type {number}
     */
    let globalLastId = -1 //last element

    /**
     * to check for new messages.
     */
    let interval1

    /**
     * to check for users connected.
     */
    let interval2

    window.onload = () =>{
        document.getElementById('bu').addEventListener('click',valiMess)
        interval1 = setInterval(checkNewMessage, 10000);
        interval2 = setInterval(checkNewUsers,   10000);
    }

    /**
     * updating the user list every 10 seconds.
     */
    function checkNewUsers(){
        fetch("/userUpdate", {method: "post"})
            .then(res => res.json())
            .then(response => {updateUsersList(response)})
            .catch((err) => {
                if(err.message === "Unexpected end of JSON input") //user disconnected
                    stopIntervalAndReturn()
                else
                    console.log(err.message)
            })
    }

    /**
     * updating for new messages every 10 seconds.
     */
    function checkNewMessage(){
        if(globalLastId === -1)
        {
            let listItem = document.querySelectorAll("li")
            let lastMess = []
            for (let i = 0; i < listItem.length; i++)
                lastMess.push(listItem[i].textContent.trim())

            if(lastMess.length === 0)
                globalLastId = -1
            else
                globalLastId = +lastMess[0].charAt(0)
        }

        fetch("/checkUpdate", {method:"post"})
            .then(res => res.json())
            .then(response => {updateList(response)})
            .catch((err) => {
                if(err.message === "Unexpected end of JSON input") //user disconnected
                    stopIntervalAndReturn()
                else
                    console.log(err.message)
            })
    }

    /**
     * updating html parameters
     * @param res
     */
    function updateUsersList(res){
        let lis = document.getElementById("onlineUsers")
        lis.innerHTML = ''
        for(let i in res){
            let node = document.createElement("p")
            let textNode = document.createTextNode(res[i])
            node.appendChild(textNode);
            lis.appendChild(node);
        }
    }

    /**
     * updating html parameters
     * @param res
     */
    function updateList(res){

        if(res.length > 0) { //check if there a new message
            if (res[res.length - 1].id === globalLastId)
                return
        }
        else
            return


        let lis = document.getElementById("messageList")
        lis.innerHTML = ''
        for(let i in res){
            let node = document.createElement("li")
            let textNode = document.createTextNode(""+res[i].userName  + " - " + res[i].message);
            node.appendChild(textNode);
            lis.appendChild(node);
            globalLastId = res[i].id
        }
    }

    /**
     * validating the message is valid and fetching it.
     * after sending message -> update the list
     * @param event
     */
    async function valiMess(event){
        event.preventDefault(); //dont refresh any way
        let mes = document.getElementById('mes').value.trim()
        document.getElementById('mes').value = ''
        if(mes === ""){
            alert("message required")
            event.preventDefault()
            return
        }

        document.getElementById("det").style.visibility = "visible"
        await fetch("/addMessage?mess="+mes, {method: "post"})
            .then(res => res.json())
            .then(response => {
                for (let i in response) {
                    if(response[i] === "disconnected")
                        stopIntervalAndReturn()
                    else if(response[i] === "message required"){
                        alert("message required")
                        return
                    }

                    document.getElementById("det").innerText = response[i]
                }
            })
            .catch(err => (console.log(err.message)))

        checkNewMessage()
    }

    /**
     * the user went offline- stop the intervals and redirect it.
     */
    function stopIntervalAndReturn(){
        clearInterval(interval1)
        clearInterval(interval2)
        window.location = `${window.location.origin}/`
    }

})(window, document, undefined)

