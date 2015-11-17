<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Api extends CI_Controller {

	public function __construct(){	
		parent::__construct();

		$this->load->model('Query_model', 'qm', TRUE);

		header('Cache-Control: no-store, no-cache, must-revalidate, max-age=0');
		header('Cache-Control: post-check=0, pre-check=0', false);
		header('Pragma: no-cache');
		header('Content-Type: application/javascript');
	}

	public function index()
	{
		show_error404();
	}

	public function post(){
		if( $this->input->post() ){

		}else{
			echo json_encode( array(
					'error' => 'Access Denied',
					'status' => '0',
				) );
		}
	}
}