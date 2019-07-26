export default {
    methods: {
        urlifyString: function (str) {
            return str.trim().replace(/\s/g, '%20');
        },
        getDDMMYYFormatFromYYYYMMDD: function (date) {
            const dateSplitted = date.split("-");

            const year = dateSplitted[0];
            const month = dateSplitted[1];
            const day = dateSplitted[2];

            return day + "-" + month + "-" + year;
        },
        convertStringToParseableDate: function (str) {
            return str.replace("/-","/")
        },
        parseDateFromDateObject: function (date) {
            let dateWithoutTime = date.split("T")[0];
            let split = dateWithoutTime.split("-");

            const day = split[0];
            const month = split[1];
            const year = split[2];

            return year + '.' + month + '.' + day
        }
    }
}
