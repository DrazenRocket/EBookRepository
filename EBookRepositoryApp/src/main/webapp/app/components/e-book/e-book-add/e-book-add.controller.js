(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .controller("EBookAddController", EBookAddController);

    EBookAddController.$inject = ["eBookService", "categoryService", "languageService", "$state", "$window"];
    function EBookAddController(eBookService, categoryService, languageService, $state, $window) {
        var viewModel = this;

        viewModel.newEBook = {};
        viewModel.newEBook.categoryId = null;
        viewModel.newEBook.languageId = null;
        viewModel.newEBook.title = "";
        viewModel.newEBook.author = "";
        viewModel.newEBook.keywords = "";
        viewModel.newEBook.publicationYear = null;
        viewModel.newEBook.filename = "";
        viewModel.newEBook.mime = "application/pdf";
        viewModel.categoryList = [];
        viewModel.languageList = [];
        viewModel.uploadingEBookFileProgress = null;
        viewModel.uploadedEBookFile = false;
        viewModel.uploadedEBookFilename = null;
        viewModel.uploadEBookFile = uploadEBookFile;
        viewModel.addEBook = addEBook;

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
                            var data  = response.data;

                            viewModel.newEBook.title = data.title;
                            viewModel.newEBook.author = data.author;
                            viewModel.newEBook.keywords = data.keywords;
                            viewModel.newEBook.filename = data.filename;
                            viewModel.newEBook.mime = mime;
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

        function addEBook(isValid) {
            if (isValid) {
                var categoryId = viewModel.newEBook.categoryId;
                var languageId = viewModel.newEBook.languageId;
                var title = viewModel.newEBook.title;
                var author = viewModel.newEBook.author;
                var keywords = viewModel.newEBook.keywords;
                var publicationYear = viewModel.newEBook.publicationYear;
                var filename = viewModel.newEBook.filename;
                var mime = viewModel.newEBook.mime;

                eBookService
                    .addEBook(categoryId, languageId, title, author, keywords, publicationYear, filename, mime)
                    .then(function (response) {
                        if (response.status == 200) {
                            var eBookId = response.data.id;
                            $window.alert("E-Book is added.");
                            $state.go("e-book-detail", {eBookId: eBookId});
                        }
                    }, function (response) {
                        $window.alert("E-Book is not added.");
                    });
            }
        }
    }
} (angular));
