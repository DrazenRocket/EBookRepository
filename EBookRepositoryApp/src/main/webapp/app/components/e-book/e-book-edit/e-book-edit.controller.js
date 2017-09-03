(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .controller("EBookEditController", EBookEditController);

    EBookEditController.$inject = ["eBookService", "categoryService", "languageService", "$state", "$stateParams", "$window"];
    function EBookEditController(eBookService, categoryService, languageService, $state, $stateParams, $window) {
        var viewModel = this;

        viewModel.eBookId = $stateParams.eBookId;
        viewModel.editedEBook = {};
        viewModel.editedEBook.categoryId = null;
        viewModel.editedEBook.languageId = null;
        viewModel.editedEBook.title = "";
        viewModel.editedEBook.author = "";
        viewModel.editedEBook.keywords = "";
        viewModel.editedEBook.publicationYear = null;
        viewModel.editedEBook.filename = "";
        viewModel.editedEBook.mime = "";
        viewModel.categoryList = [];
        viewModel.languageList = [];
        viewModel.useNewEBookFile = false;
        viewModel.uploadingEBookFileProgress = null;
        viewModel.uploadedEBookFile = false;
        viewModel.uploadedEBookFilename = null;
        viewModel.uploadEBookFile = uploadEBookFile;
        viewModel.editEBook = editEBook;

        eBookService
            .getEBookById(viewModel.eBookId)
            .then(function (response) {
                if (response.status = 200) {
                    var data = response.data;

                    viewModel.editedEBook.categoryId = data.category.id;
                    viewModel.editedEBook.languageId = data.language.id;
                    viewModel.editedEBook.title = data.title;
                    viewModel.editedEBook.author = data.author;
                    viewModel.editedEBook.keywords = data.keywords;
                    viewModel.editedEBook.publicationYear = data.publicationYear;
                    viewModel.editedEBook.filename = data.filename;
                    viewModel.editedEBook.mime = data.mime;
                }
            });

        categoryService
            .getAllCategories()
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.categoryList = response.data;
                }
            });

        languageService
            .getAllLanguages()
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.languageList = response.data;
                }
            });

        function uploadEBookFile(eBookFile) {
            if (eBookFile) {
                var mime = "application/pdf";
                
                eBookService
                    .uploadEBookFile(eBookFile, mime)
                    .then(function (response) {
                        viewModel.uploadingEBookFileProgress = null;
                        viewModel.uploadedEBookFile = true;

                        if (response.status == 200) {
                            var data = response.data;

                            viewModel.useNewEBookFile = true;
                            viewModel.editedEBook.title = data.title;
                            viewModel.editedEBook.author = data.author;
                            viewModel.editedEBook.keywords = data.keywords;
                            viewModel.editedEBook.filename = data.filename;
                            viewModel.editedEBook.mime = mime;
                            viewModel.uploadedEBookFilename = eBookFile.name;
                        }
                    }, function (response) {
                        viewModel.uploadingEBookFileProgress = null;
                        viewModel.uploadedEBookFile = false;
                        $window.alert("File is not uploaded.");
                    }, function (notify) {
                        viewModel.uploadingEBookFileProgress = parseInt(100.0 * notify.loaded / notify.total);
                    });
            }
        }

        function editEBook(isValid) {
            if (isValid) {
                var eBookId = viewModel.eBookId;
                var categoryId = viewModel.editedEBook.categoryId;
                var languageId = viewModel.editedEBook.languageId;
                var title = viewModel.editedEBook.title;
                var author = viewModel.editedEBook.author;
                var keywords = viewModel.editedEBook.keywords;
                var publicationYear = viewModel.editedEBook.publicationYear;
                var filename = viewModel.editedEBook.filename;
                var mime = viewModel.editedEBook.mime;

                eBookService
                    .editEBook(eBookId, categoryId, languageId, title, author, keywords, publicationYear, filename, mime)
                    .then(function (response) {
                        if (response.status == 204) {
                            $window.alert("E-Book is edited.");
                            $state.go("e-book-detail", {eBookId: eBookId});
                        }
                    }, function (response) {
                        if (response.status == 404) {
                            $window.alert("E-Book does not exist.");
                            $state.go("e-book-list");
                        }
                        else {
                            $window.alert("E-Book is not edited.")
                        }
                    });
            }
        }
    }
} (angular));
