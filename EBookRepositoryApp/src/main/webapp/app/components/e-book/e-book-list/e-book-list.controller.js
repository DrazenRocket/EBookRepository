(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .controller("EBookListController", EBookListController);

    EBookListController.$inject = ["eBookService", "userService"];
    function EBookListController(eBookService, userService) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.eBookList = [];

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
    }
} (angular));
