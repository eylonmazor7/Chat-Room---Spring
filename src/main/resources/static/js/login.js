(function (window, document, undefined) {
    window.onload = () =>{
        /**
         * a listener assigned to validate the user when click happend.
         */
        document.getElementById('bu').addEventListener('click',valiUser)
    }

    /**
     * Trimming the user name and validating it
     * @param event
     */
    function valiUser(event){
        let checkName = document.getElementById('name').value.trim()
        if(checkName === '') {
            alert("Name is required")
            event.preventDefault();
        }
    }
})(window, document, undefined)

