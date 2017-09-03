(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .controller("EBookDetailController", EBookDetailController);

    EBookDetailController.$inject = ["eBookService", "userService", "$state", "$stateParams"]
    function EBookDetailController(eBookService, userService, $state, $stateParams) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.eBookId = $stateParams.eBookId;
        viewModel.eBookDetails = {};
        viewModel.editEBook = editEBook;
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

        eBookService
            .getEBookById(viewModel.eBookId)
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.eBookDetails = response.data;
                }
            });

        function editEBook(eBookId) {
            $state.go("e-book-edit", {eBookId: eBookId});
        }

        function downloadEBookFile(eBookId) {
            eBookService
                .downloadEBookFileByEBookId(eBookId);
        }
    }
} (angular));
