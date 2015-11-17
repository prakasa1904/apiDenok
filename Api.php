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
		show_404();
	}

	public function list_lokasi(){
		if( $this->input->get('limit') )
			$data = $this->qm->get_lokasi_apps( $this->input->get('limit') );
		else
			$data = $this->qm->get_lokasi_apps();
			
			echo json_encode( $data );
	}

	public function post(){
		// Data Post API 
		if( $this->input->post() ){
			if ( $this->qm->addAset() )
				echo json_encode( array(
					'status' => '1',
				) );
		}else{
			echo json_encode( array(
					'status' => '0',
				) );
		}
	}
}