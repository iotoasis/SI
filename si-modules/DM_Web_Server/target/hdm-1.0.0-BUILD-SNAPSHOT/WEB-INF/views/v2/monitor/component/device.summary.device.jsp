<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">디바이스 정보</h3>
							<span class="pull-right" style="margin-top: -16px;">등록 : <span id="dsd_regdate">-</span></span>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<div class="row">
								<span class="small-title">SN</span>
								<h2 class="no-margins font-bold" id="dsd_sn">-</h2>
								<br/>
								<span class="small-title">FW</span>
								<h2 class="no-margins font-bold" style="float: left;" id="dsd_fw_package"></h2>
								<span class="span-fw">ver:<span id="dsd_fw_version">-</span></span>
								
								<!-- <span class="small-date">등록</span>
								<div class="div-date">2015.02.10</div> -->
							</div>
							<div class="row">
								<span style="margin-left: 45px;" id="dsd_fw_update_date"></span>
								<!-- <button type="button" class="btn btn-primary btn-smalls-size" id="dsd_fw_update_btn"><small class="btn-smalls-text"></small></button> -->
								<a data-toggle="modal" class="btn btn-primary btn-smalls-size" href="#dsd_fw_update_form"><small class="btn-smalls-text">버전업</small></a>                            
							</div>
							
                            <div id="dsd_fw_update_form" class="modal fade" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-sm-12"><h3 class="m-t-none m-b">펌웨어 업그레이드</h3>

                                                    <p>업그레이드할 버전을 선택해 주세요.</p>

                                                        <div class="form-group">
	                                                        <select class="form-control m-b" name="version" id="dsd_fw_version_option">
						                                        <option value="">option 1</option>
						                                        <option value="">option 2</option>
						                                        <option value="">option 3</option>
						                                        <option value="">option 4</option>
						                                    </select>
                                                        </div>
                                                        <div>
                                                            <button id="dsd_fw_submit_btn" class="btn btn-sm btn-primary pull-right m-t-n-xs disabled" type="submit"><strong>업그레이드 실행</strong></button>
                                                        </div>
                                                </div>
	                                        </div>
	                                    </div>
	                            	</div>
	                            </div>
	                    	</div>    
						</div>
					</div>