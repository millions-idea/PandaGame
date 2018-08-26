window.utils = {
    response: {
        isError: function (data) {
            return data == null || data.error == null || data.error == 1;
        }
    }
}