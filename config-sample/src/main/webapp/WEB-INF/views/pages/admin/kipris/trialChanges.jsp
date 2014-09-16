<%@ page contentType="text/html; charset=UTF-8" %>

<div class="container">
    <div class="page-header">
        <h1>소송변경사항</h1>
    </div>

    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-1 control-label">작업명</label>
                    <div class="col-sm-5">
                        <p class="form-control-static">{{task.name}}</p>
                    </div>
                    <label class="col-sm-1 control-label">현재상태</label>
                    <div class="col-sm-5">
                        <p class="form-control-static">{{taskStateToString(task.state)}}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-1 control-label">시작시간</label>
                    <div class="col-sm-5">
                        <p class="form-control-static">{{task.startedAt | date:'yyyy-MM-dd hh:mm:ss.sss'}}</p>
                    </div>
                    <label class="col-sm-1 control-label">종료시간</label>
                    <div class="col-sm-5">
                        <p class="form-control-static">{{task.endedAt | date:'yyyy-MM-dd hh:mm:ss.sss'}}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-1 control-label">결과</label>
                    <div class="col-sm-9">
                        <p class="form-control-static">전체 {{totalCount}}건 중에 성공 {{successCount}}건, 실패 {{failureCount}}건, 진행 및 대기 {{restCount}}건</p>
                    </div>
                    <button class="btn btn-default" type="button" ng-click="recollectFailure()" ng-disabled="failureCount === 0">실패 재수집!</button>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-6">
            <h4>1심</h4>
            <accordion>
                <accordion-group heading="신규 ({{firstNewChanges.length}})" is-open="status.isFirstOpen" is-disabled="status.isFirstDisabled">
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr class="bg-primary">
                            <th>번호</th>
                            <th>소송번호</th>
                            <th>수집상태</th>
                            <th>적용</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-show="firstNewChanges.length == 0">
                            <td colspan="4" class="text-center">조회된 데이타가 없습니다.</td>
                        </tr>
                        <tr ng-repeat="change in firstNewChanges">
                            <td>{{$index + 1}}</td>
                            <td>{{change.trialNo}}</td>
                            <td>{{taskStateToString(change.taskState)}}</td>
                            <td>
                                <button class="btn btn-default" type="button" ng-click="apply(change, firstNewChanges)" ng-disabled="!isAppliable(change)">적용</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </accordion-group>
                <accordion-group heading="심결 변경 ({{firstDecisionChanges.length}})" is-disabled="firstDecisionChanges.length === 0">
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr class="bg-primary">
                            <th>번호</th>
                            <th>소송번호</th>
                            <th>수집상태</th>
                            <th>적용</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-show="firstDecisionChanges.length == 0">
                            <td colspan="4" class="text-center">조회된 데이타가 없습니다.</td>
                        </tr>
                        <tr ng-repeat="change in firstDecisionChanges">
                            <td>{{$index + 1}}</td>
                            <td>{{change.trialNo}}</td>
                            <td>{{taskStateToString(change.taskState)}}</td>
                            <td>
                                <button class="btn btn-default" type="button" ng-click="apply(change, firstDecisionChanges)" ng-disabled="!isAppliable(change)">적용</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </accordion-group>
                <accordion-group heading="일반 변경 ({{firstGeneralChanges.length}})" is-disabled="firstGeneralChanges.length === 0">
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr class="bg-primary">
                            <th>번호</th>
                            <th>소송번호</th>
                            <th>수집상태</th>
                            <th>적용</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-show="firstGeneralChanges.length == 0">
                            <td colspan="4" class="text-center">조회된 데이타가 없습니다.</td>
                        </tr>
                        <tr ng-repeat="change in firstGeneralChanges">
                            <td>{{$index + 1}}</td>
                            <td>{{change.trialNo}}</td>
                            <td>{{taskStateToString(change.taskState)}}</td>
                            <td>
                                <button class="btn btn-default" type="button" ng-click="apply(change, firstGeneralChanges)" ng-disabled="!isAppliable(change)">적용</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </accordion-group>
            </accordion>
        </div>
        <div class="col-sm-6">
            <h4>2,3심</h4>
            <accordion>
                <accordion-group heading="신규 ({{courtNewChanges.length}})" is-disabled="courtNewChanges.length === 0">
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr class="bg-primary">
                            <th>번호</th>
                            <th>소송번호</th>
                            <th>수집상태</th>
                            <th>적용</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-show="courtNewChanges.length == 0">
                            <td colspan="4" class="text-center">조회된 데이타가 없습니다.</td>
                        </tr>
                        <tr ng-repeat="change in courtNewChanges">
                            <td>{{$index + 1}}</td>
                            <td>{{change.trialNo}}</td>
                            <td>{{taskStateToString(change.taskState)}}</td>
                            <td>
                                <button class="btn btn-default" type="button" ng-click="apply(change, courtNewChanges)" ng-disabled="!isAppliable(change)">적용</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </accordion-group>
                <accordion-group heading="심결 변경 ({{courtDecisionChanges.length}})" is-disabled="courtDecisionChanges.length === 0">
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr class="bg-primary">
                            <th>번호</th>
                            <th>소송번호</th>
                            <th>수집상태</th>
                            <th>적용</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-show="courtDecisionChanges.length == 0">
                            <td colspan="4" class="text-center">조회된 데이타가 없습니다.</td>
                        </tr>
                        <tr ng-repeat="change in courtDecisionChanges">
                            <td>{{$index + 1}}</td>
                            <td>{{change.trialNo}}</td>
                            <td>{{taskStateToString(change.taskState)}}</td>
                            <td>
                                <button class="btn btn-default" type="button" ng-click="apply(change, courtDecisionChanges)" ng-disabled="!isAppliable(change)">적용</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </accordion-group>
                <accordion-group heading="일반 변경 ({{courtGeneralChanges.length}})" is-disabled="courtGeneralChanges.length === 0">
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr class="bg-primary">
                            <th>번호</th>
                            <th>소송번호</th>
                            <th>수집상태</th>
                            <th>적용</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-show="courtGeneralChanges.length == 0">
                            <td colspan="4" class="text-center">조회된 데이타가 없습니다.</td>
                        </tr>
                        <tr ng-repeat="change in courtGeneralChanges">
                            <td>{{$index + 1}}</td>
                            <td>{{change.trialNo}}</td>
                            <td>{{taskStateToString(change.taskState)}}</td>
                            <td>
                                <button class="btn btn-default" type="button" ng-click="apply(change, courtGeneralChanges)" ng-disabled="!isAppliable(change)">적용</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </accordion-group>
            </accordion>
        </div>
    </div>

</div>

