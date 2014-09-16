<%@ page contentType="text/html; charset=UTF-8" %>

<div class="container">
    <div class="page-header">
        <h1>특허관리</h1>
    </div>

    <div class="panel panel-default">
        <div class="panel-body">
            <form class="form-horizontal" role="form" ng-submit="performSearch()">
                <div class="form-group">
                    <label for="patentNo" class="col-sm-1 control-label">특허번호</label>
                    <div class="col-sm-11">
                        <input id="patentNo" name="patentNo" type="text" class="form-control" placeholder="특허번호" ng-model="search.form.patentNo">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-1 col-sm-11">
                        <button type="button" class="btn btn-default" ng-click="resetSearch()">초기화</button>
                        <button type="submit" class="btn btn-primary">검색</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <!--
        <div class="panel-heading">head</div>
        <div class="panel-body">body</div>
        -->

        <table class="table table-bordered table-striped">
            <thead>
            <tr class="bg-primary">
                <th>번호</th>
                <th>특허번호</th>
                <th>공개원문</th>
                <th>등록원부</th>
                <th>정정공보</th>
                <th>소송관계도</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-show="patents.length == 0">
                <td colspan="6" class="text-center">조회된 데이타가 없습니다.</td>
            </tr>
            <tr ng-repeat="patent in patents">
                <td>{{pagination.reverseNo - $index}}</td>
                <td><a href="" ng-click="detail(patent.no)">{{patent.no}}</a></td>
                <td>{{patent.originalOpenFileName}}</td>
                <td>{{patent.originalRegisterFileName}}</td>
                <td>{{patent.correctionFileName}}</td>
                <td>{{patent.allLawsuitImageName}}</td>
            </tr>
            </tbody>
        </table>
    </div>

    <pagination total-items="pagination.totalCount"
                ng-model="pagination.currentPage"
                max-size="pagination.maxSize"
                class="pagination-sm"
                boundary-links="true"
                ng-change="pageChanged()">
    </pagination>

</div>

<script type="text/ng-template" id="admin/info/patent/detail.html">
    <div class="modal-header">
        <h3 class="modal-title">특허상세</h3>
    </div>
    <div class="modal-body">
        <form id="patentForm" name="patentForm" class="form-horizontal" role="form" ng-submit="submit()">
            <div class="form-group">
                <label for="detail-no" class="col-sm-2 control-label">특허번호</label>
                <div class="col-sm-10">
                    <input id="detail-no" name="no" type="text" class="form-control" placeholder="특허번호" ng-readonly="true" ng-model="model.no" ng-if="!no">
                    <p class="form-control-static" ng-if="!!no">{{model.no}}</p>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-originalOpenFile" class="col-sm-2 control-label">공개원문파일</label>
                <div class="col-sm-8">
                    <div ng-if="!model.originalOpenFileName">
                        <input id="detail-originalOpenFile" name="originalOpenFile" type="file" class="form-control" placeholder="공개원문파일" ng-file-select="selectFile('originalOpenFile', $files)">
                        <progressbar class="active" value="uploadOptions.originalOpenFile.progress" type="{{uploadOptions.originalOpenFile.success ? 'success' : 'danger'}}" ng-if="uploadOptions.originalOpenFile.progress > 0">
                            <i ng-if="!uploadOptions.originalOpenFile.success">업로드실패</i>
                            <i ng-if="uploadOptions.originalOpenFile.success">{{uploadOptions.originalOpenFile.progress}}%</i>
                        </progressbar>
                    </div>
                    <div ng-if="model.originalOpenFileName" class="form-control-static">
                        <a href="/common/download/patent/{{model.no}}?type=originalOpenFile">{{model.originalOpenFileName}}</a>
                        <button type="button" class="btn btn-warning" ng-click="deleteFile('originalOpenFile')">삭제</button>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-originalRegisterFile" class="col-sm-2 control-label">등록원문파일</label>
                <div class="col-sm-8">
                    <div ng-if="!model.originalRegisterFileName">
                        <input id="detail-originalRegisterFile" name="originalRegisterFile" type="file" class="form-control" placeholder="등록원문파일" ng-file-select="selectFile('originalRegisterFile', $files)">
                        <progressbar class="active" value="uploadOptions.originalRegisterFile.progress" type="{{uploadOptions.originalRegisterFile.success ? 'success' : 'danger'}}" ng-if="uploadOptions.originalRegisterFile.progress > 0">
                            <i ng-if="!uploadOptions.originalRegisterFile.success">업로드실패</i>
                            <i ng-if="uploadOptions.originalRegisterFile.success">{{uploadOptions.originalRegisterFile.progress}}%</i>
                        </progressbar>
                    </div>
                    <div ng-if="model.originalRegisterFileName" class="form-control-static">
                        <a href="/common/download/patent/{{model.no}}?type=originalRegisterFile">{{model.originalRegisterFileName}}</a>
                        <button type="button" class="btn btn-warning" ng-click="deleteFile('originalRegisterFile')">삭제</button>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-correctionFileName" class="col-sm-2 control-label">정정원문파일</label>
                <div class="col-sm-8">
                    <input id="detail-correctionFileName" name="correctionFileName" type="text" class="form-control" placeholder="특허번호" ng-readonly="true" ng-model="model.correctionFileName">
                </div>
                <button class="btn btn-warning col-sm-1">삭제</button>
            </div>
            <div class="form-group">
                <label for="detail-allLawsuitImageName" class="col-sm-2 control-label">소송관계도파일</label>
                <div class="col-sm-8">
                    <input id="detail-allLawsuitImageName" name="allLawsuitImageName" type="text" class="form-control" placeholder="특허번호" ng-readonly="true" ng-model="model.allLawsuitImageName">
                </div>
                <button class="btn btn-warning col-sm-1">삭제</button>
            </div>
            <div class="form-group">
                <label for="detail-name" class="col-sm-2 control-label">발명의명칭</label>
                <div class="col-sm-10">
                    <textarea id="detail-name" name="name" class="form-control" placeholder="발명의명칭" rows="5">{{model.name}}</textarea>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-applicationNo" class="col-sm-2 control-label">출원번호</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-applicationNo" name="applicationNo" class="form-control" placeholder="출원번호" ng-model="model.applicationNo"/>
                </div>
                <label for="detail-applicationDate" class="col-sm-2 control-label">출원일</label>
                <div class="col-sm-4">
                    <p class="input-group">
                        <input type="text" id="detail-applicationDate" name="applicationDate" class="form-control" placeholder="YYYY-MM-DD" ng-model="model.applicationDate" datepicker-popup datepicker-pattern is-open="$parent.datepickerOptions.opened.applicationDate"/>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default" ng-click="openDatepicker($event, 'applicationDate')"><i class="glyphicon glyphicon-calendar"></i></button>
                        </span>
                    </p>
                    <div class="error-container" ng-show="patentForm.applicationDate.$dirty && patentForm.applicationDate.$invalid">
                        <small class="error" ng-show="patentForm.applicationDate.$error.pattern || patentForm.applicationDate.$error.date">날짜를 정확히 입력해 주세요</small>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-applicationNo" class="col-sm-2 control-label">출원인</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-applicant" name="applicant" class="form-control" placeholder="출원인" ng-model="model.applicant"/>
                </div>
                <label for="detail-priorityCountry" class="col-sm-2 control-label">우선권국가</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-priorityCountry" name="priorityCountry" class="form-control" placeholder="우선권국가" ng-model="model.priorityCountry"/>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-priorityNo" class="col-sm-2 control-label">우선권번호</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-priorityNo" name="priorityNo" class="form-control" placeholder="우선권번호" ng-model="model.priorityNo"/>
                </div>
                <label for="detail-priorityDate" class="col-sm-2 control-label">우선권주장일</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-priorityDate" name="priorityDate" class="form-control" placeholder="우선권주장일" ng-model="model.priorityDate"/>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-abstracts" class="col-sm-2 control-label">요약</label>
                <div class="col-sm-10">
                    <textarea id="detail-abstracts" name="abstracts" class="form-control" placeholder="요약" rows="5">{{model.abstracts}}</textarea>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-exemplaryClaim" class="col-sm-2 control-label">대표청구항</label>
                <div class="col-sm-10">
                    <textarea id="detail-exemplaryClaim" name="exemplaryClaim" class="form-control" placeholder="대표청구항" rows="5">{{model.exemplaryClaim}}</textarea>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-publicationNo" class="col-sm-2 control-label">공개번호</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-publicationNo" name="publicationNo" class="form-control" placeholder="공개번호" ng-model="model.publicationNo"/>
                </div>
                <label for="detail-publicationDate" class="col-sm-2 control-label">공개일</label>
                <div class="col-sm-4">
                    <p class="input-group">
                        <input type="text" id="detail-publicationDate" name="publicationDate" class="form-control" placeholder="YYYY-MM-DD" ng-model="model.publicationDate" datepicker-popup datepicker-pattern is-open="$parent.datepickerOptions.opened.publicationDate"/>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default" ng-click="openDatepicker($event, 'publicationDate')"><i class="glyphicon glyphicon-calendar"></i></button>
                        </span>
                    </p>
                    <div class="error-container" ng-show="patentForm.publicationDate.$dirty && patentForm.publicationDate.$invalid">
                        <small class="error" ng-show="patentForm.publicationDate.$error.pattern || patentForm.publicationDate.$error.date">날짜를 정확히 입력해 주세요</small>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-date" class="col-sm-2 control-label">등록일</label>
                <div class="col-sm-4">
                    <p class="input-group">
                        <input type="text" id="detail-date" name="date" class="form-control" placeholder="YYYY-MM-DD" ng-model="model.date" datepicker-popup datepicker-pattern is-open="$parent.datepickerOptions.opened.date"/>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default" ng-click="openDatepicker($event, 'date')"><i class="glyphicon glyphicon-calendar"></i></button>
                        </span>
                    </p>
                    <div class="error-container" ng-show="patentForm.date.$dirty && patentForm.date.$invalid">
                        <small class="error" ng-show="patentForm.date.$error.pattern || patentForm.date.$error.date">날짜를 정확히 입력해 주세요</small>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-pctApplicationNo" class="col-sm-2 control-label">국제출원번호</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-pctApplicationNo" name="pctApplicationNo" class="form-control" placeholder="국제출원번호" ng-model="model.pctApplicationNo"/>
                </div>
                <label for="detail-pctApplicationDate" class="col-sm-2 control-label">국제출원일</label>
                <div class="col-sm-4">
                    <p class="input-group">
                        <input type="text" id="detail-pctApplicationDate" name="pctApplicationDate" class="form-control" placeholder="YYYY-MM-DD" ng-model="model.pctApplicationDate" datepicker-popup datepicker-pattern is-open="$parent.datepickerOptions.opened.pctApplicationDate"/>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default" ng-click="openDatepicker($event, 'pctApplicationDate')"><i class="glyphicon glyphicon-calendar"></i></button>
                        </span>
                    </p>
                    <div class="error-container" ng-show="patentForm.pctApplicationDate.$dirty && patentForm.pctApplicationDate.$invalid">
                        <small class="error" ng-show="patentForm.pctApplicationDate.$error.pattern || patentForm.pctApplicationDate.$error.date">날짜를 정확히 입력해 주세요</small>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-pctPublicationNo" class="col-sm-2 control-label">국제공개번호</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-pctPublicationNo" name="date" class="form-control" placeholder="국제공개번호" ng-model="model.pctPublicationNo"/>
                </div>
                <label for="detail-pctPublicationDate" class="col-sm-2 control-label">국제공개일</label>
                <div class="col-sm-4">
                    <p class="input-group">
                        <input type="text" id="detail-pctPublicationDate" name="pctPublicationDate" class="form-control" placeholder="YYYY-MM-DD" ng-model="model.pctPublicationDate" datepicker-popup datepicker-pattern is-open="$parent.datepickerOptions.opened.pctPublicationDate"/>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default" ng-click="openDatepicker($event, 'pctPublicationDate')"><i class="glyphicon glyphicon-calendar"></i></button>
                        </span>
                    </p>
                    <div class="error-container" ng-show="patentForm.pctPublicationDate.$dirty && patentForm.pctPublicationDate.$invalid">
                        <small class="error" ng-show="patentForm.pctPublicationDate.$error.pattern || patentForm.pctPublicationDate.$error.date">날짜를 정확히 입력해 주세요</small>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="detail-country" class="col-sm-2 control-label">지정국코드</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-country" name="date" class="form-control" placeholder="지정국코드" ng-model="model.country"/>
                </div>
                <label for="detail-wipsOnKey" class="col-sm-2 control-label">WIPS On Key</label>
                <div class="col-sm-4">
                    <input type="text" id="detail-wipsOnKey" name="date" class="form-control" placeholder="WIPS On Key" ng-model="model.wipsOnKey"/>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" ng-click="submit()">저장</button>
        <button class="btn btn-warning" ng-click="close()">닫기</button>
    </div>
</script>

