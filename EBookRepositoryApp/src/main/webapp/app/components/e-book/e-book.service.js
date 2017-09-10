(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .service("eBookService", EBookService);

    EBookService.$inject = ["$http", "$window", "Upload", "userService"];
    function EBookService($http, $window, Upload, userService) {
        this.$http = $http;
        this.$window = $window;
        this.Upload = Upload;
        this.userService = userService;
    }

    EBookService.prototype.getEBookById = function (eBookId) {
        var request = {
            method: "GET",
            url: "/api/ebooks/" + eBookId
        };

        return this.$http(request);
    };

    EBookService.prototype.getAllEBooks = function () {
        var request = {
            method: "GET",
            url: "/api/ebooks"
        };

        return this.$http(request);
    };

    EBookService.prototype.getAllEBooksByCategoryId = function (categoryId) {
        var request = {
            method: "GET",
            url: "/api/categories/" + categoryId + "/ebooks"
        };

        return this.$http(request);
    };

    EBookService.prototype.getEBookFileByEBookId = function (eBookId) {
        var jwt = this.userService.getJwtFromLocalStorage();
        var request = {
            method: "GET",
            url: "/api/ebooks/" + eBookId + "/file",
            headers: {
                "X-Auth-Jwt": jwt
            },
            responseType: "arraybuffer"
        };

        return this.$http(request);
    };

    EBookService.prototype.addEBook = function (categoryId, languageId, title, author, keywords, publicationYear, filename, mime) {
        var jwt = this.userService.getJwtFromLocalStorage();
        var request = {
            method: "POST",
            url: "/api/ebooks",
            headers: {
                "X-Auth-Jwt": jwt
            },
            data: {
                categoryId: categoryId,
                languageId: languageId,
                title: title,
                author: author,
                keywords: keywords,
                publicationYear: publicationYear,
                filename: filename,
                mime: mime
            }
        };

        return this.$http(request);
    };

    EBookService.prototype.editEBook = function(eBookId, categoryId, languageId, title, author, keywords, publicationYear, filename, mime) {
        var jwt = this.userService.getJwtFromLocalStorage();
        var request = {
            method: "PUT",
            url: "/api/ebooks/" + eBookId,
            headers: {
                "X-Auth-Jwt": jwt
            },
            data: {
                categoryId: categoryId,
                languageId: languageId,
                title: title,
                author: author,
                keywords: keywords,
                publicationYear: publicationYear,
                filename: filename,
                mime: mime
            }
        };

        return this.$http(request);
    };

    EBookService.prototype.uploadEBookFile = function (eBookFile, mime) {
        var jwt = this.userService.getJwtFromLocalStorage();
        var request = {
            url: "/api/ebooks/files",
            method: "POST",
            headers: {
                "X-Auth-Jwt": jwt
            },
            data: {
                file: eBookFile
            }
        };

        return this.Upload.upload(request);
    };

    EBookService.prototype.downloadEBookFileByEBookId = function (eBookId) {
        var thisEBookService = this;

        this.getEBookFileByEBookId(eBookId)
            .then(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    var dataBlob = new Blob([data], {type: 'application/pdf'});         //Try to use something generic
                    var aElement = document.createElement("a");                         //
                    var fileUrl = window.URL.createObjectURL(dataBlob);                 //

                    aElement.href = fileUrl;
                    aElement.download = "EBook.pdf";
                    aElement.click();
                }
            }, function (response) {
                if (response.status == 404) {
                    thisEBookService.$window.alert("File can't be found.");
                }
                else {
                    thisEBookService.$window.alert("File can't be downloaded.");
                }
            });
    };

    EBookService.prototype.searchEBooksBySingleField = function (fieldName, fieldValue, queryType) {
        var request = {
            method: "GET",
            url: "/api/ebooks/searchBySingleField",
            params: {
                fieldName: fieldName,
                fieldValue: fieldValue,
                queryType: queryType
            }
        };

        return this.$http(request);
    };
    
    EBookService.prototype.searchEBooksByMultiFields = function (title, author, keywords, content, language, queryOperator, queryType) {
        var request = {
            method: "GET",
            url: "/api/ebooks/searchByMultiFields",
            params: {
                title: title,
                author: author,
                keywords: keywords,
                content: content,
                language: language,
                queryOperator: queryOperator,
                queryType: queryType
            }
        };
        
        return this.$http(request);
    };
} (angular));
