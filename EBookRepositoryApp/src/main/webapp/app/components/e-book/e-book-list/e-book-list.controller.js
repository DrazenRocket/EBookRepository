(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .controller("EBookListController", EBookListController);

    EBookListController.$inject = ["eBookService", "userService", "$state"];
    function EBookListController(eBookService, userService, $state) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.eBookList = [];
        viewModel.showEBookDetails = showEBookDetails;

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

        eBookService
            .getAllEBooks()
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.eBookList = response.data;
                }
            });

        function showEBookDetails(eBookId) {
            $state.go("e-book-detail", {eBookId: eBookId});
        }
    }
} (angular));
