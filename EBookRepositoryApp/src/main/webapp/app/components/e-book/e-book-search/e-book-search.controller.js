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
        viewModel.searchParameters = {};
        viewModel.searchParameters.title = "";
        viewModel.searchParameters.author = "";
        viewModel.searchParameters.keywords = "";
        viewModel.searchParameters.content = "";
        viewModel.searchParameters.language = "";
        viewModel.searchParameters.operator = "OR";
        viewModel.searchResults = null;
        viewModel.searchEBooks = searchEBooks;
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

        function searchEBooks(isValid) {
            if (isValid) {
                viewModel.searchResults = null;
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
