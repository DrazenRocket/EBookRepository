(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .controller("EBookSearchController", EBookSearchController);

    EBookSearchController.$inject = ["eBookService", "userService", "$state"];
    function EBookSearchController(eBookService, userService, $state) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.singleFieldSearchParameters = {};
        viewModel.singleFieldSearchParameters.fieldName = "title";
        viewModel.singleFieldSearchParameters.fieldValue = "";
        viewModel.singleFieldSearchParameters.queryType = "STANDARD";
        viewModel.multiFieldSearchParameters = {};
        viewModel.multiFieldSearchParameters.title = "";
        viewModel.multiFieldSearchParameters.author = "";
        viewModel.multiFieldSearchParameters.keywords = "";
        viewModel.multiFieldSearchParameters.content = "";
        viewModel.multiFieldSearchParameters.language = "";
        viewModel.multiFieldSearchParameters.queryOperator = "OR";
        viewModel.multiFieldSearchParameters.queryType = "STANDARD";
        viewModel.searchResults = null;
        viewModel.searchEBooksBySingleField = searchEBooksBySingleField;
        viewModel.searchEBooksByMultiFields = searchEBooksByMultiFields;
        viewModel.showEBookDetails = showEBookDetails;
        viewModel.downloadEBookFile = downloadEBookFile;

        if (viewModel.loggedIn) {
            viewModel.loggedInUserId = userService.getUserIdFromLocalStorage();
            userService
                .getUserById(viewModel.loggedInUserId)
                .then(function (response) {
                    if (response.status == 200) {
                        viewModel.loggedInUserDetails = response.data;
                    }
                });
        }

        function searchEBooksBySingleField(isValid) {
            if (isValid) {
                var fieldName = viewModel.singleFieldSearchParameters.fieldName;
                var fieldValue = viewModel.singleFieldSearchParameters.fieldValue;
                var queryType = viewModel.singleFieldSearchParameters.queryType;

                viewModel.searchResults = null;

                eBookService
                    .searchEBooksBySingleField(fieldName, fieldValue, queryType)
                    .then(function (response) {
                        if (response.status == 200) {
                            viewModel.searchResults = response.data;
                        }
                        else {
                            viewModel.searchResults = [];
                        }
                    }, function (response) {
                        viewModel.searchResults = [];
                    });
            }
        }

        function searchEBooksByMultiFields(isValid) {
            if (isValid) {
                var title = viewModel.multiFieldSearchParameters.title;
                var author = viewModel.multiFieldSearchParameters.author;
                var keywords = viewModel.multiFieldSearchParameters.keywords;
                var content = viewModel.multiFieldSearchParameters.content;
                var language = viewModel.multiFieldSearchParameters.language;
                var queryOperator = viewModel.multiFieldSearchParameters.queryOperator;
                var queryType = viewModel.multiFieldSearchParameters.queryType;

                viewModel.searchResults = null;

                eBookService
                    .searchEBooksByMultiFields(title, author, keywords, content, language, queryOperator, queryType)
                    .then(function (response) {
                        if (response.status == 200) {
                            viewModel.searchResults = response.data;
                        }
                        else {
                            viewModel.searchResults = [];
                        }
                    }, function (response) {
                        viewModel.searchResults = [];
                    })
            }
        }

        function showEBookDetails(eBookId) {
            $state.go("e-book-detail", {eBookId: eBookId});
        }

        function downloadEBookFile(eBookId) {
            eBookService
                .downloadEBookFileByEBookId(eBookId);
        }
    }
} (angular));
