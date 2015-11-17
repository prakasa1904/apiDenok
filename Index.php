<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Index extends CI_Controller {

	public function __construct(){
		parent::__construct();

		$this->load->model('model_article');
	}

	public function index()
	{
	}
}
