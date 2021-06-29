(function (window, document, undefined) {
    window.onload = () =>{
        document.getElementById('bu').addEventListener('click',valiUser)
    }

    /**
     * validating the text to search
     * @param event
     */
    function valiUser(event){
        let checkText = document.getElementById('text').value.trim()
        let selectValue = document.getElementById("searchOptionsSelect").value
        if(checkText === '' || selectValue === '') {
            alert("text required")
            return
        }

        fetch("/searchText", {
            headers: {'Content-Type': 'application/json' },method:"post",
            body: JSON.stringify({
                searchOptions: selectValue,
                message : checkText
            })
        })
            .then(res => res.json())
            .then(response => {
                //if the result is empty -> display message : "no result"
                insertResult(response, selectValue)
            }).catch((err) => {
            if(err.message === "Unexpected end of JSON input") //user disconnected
                window.location = `${window.location.origin}/` //client redirect
            else
                console.log(err.message)
        })
        event.preventDefault(); //dont refresh any way
    }

    /**
     * create html elements which will contain the returned data
     * @param res - response received
     * @param selectValue - searching option
     */
    function insertResult(res, selectValue){
        let result = document.getElementById('result')
        let lis = document.getElementById("resultList")

        result.style.visibility = 'hidden'

        lis.innerHTML = '' //need to delete the last results
        let textnode = ""
        for(let i in res){
            if(result.style.visibility === 'hidden')
                result.style.visibility = 'visible'

            let node = document.createElement("li");
            if(selectValue === "user name") textnode = document.createTextNode(res[i].message);
            else textnode = document.createTextNode("Written by: " + res[i].userName  + " - " + res[i].message);
            node.appendChild(textnode);
            lis.appendChild(node);
        }

        if(result.style.visibility === 'hidden') {
            result.style.visibility = 'visible'
            let node = document.createElement("p");
            textnode = document.createTextNode("no result");
            node.appendChild(textnode);
            lis.appendChild(node);
        }
    }

})(window, document, undefined)
