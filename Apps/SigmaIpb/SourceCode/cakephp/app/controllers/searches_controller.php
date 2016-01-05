<?php
Class SearchesController extends AppController {
	var $name = 'Searches';
	
	function index() {
		if (!empty ($this->data)) {
			$field	 = $this->data['Search']['field'];
			$keyword = $this->data['Search']['keyword'];
			$options = array (
				'fields' => array('Search.nim','Search.nama','Search.kota'),
				'conditions' => array (
					$field.' LIKE' => '%' . $keyword . '%'
					)
				);
			
			$result = $this->Search->find('all', $options);
			$this->set('result', $result);
			$this->set('keyword', $keyword);
		}
	}
}
?>