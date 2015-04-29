using UnityEngine;
using System.Collections;

public class CharacterControl : MonoBehaviour {

	private Vector3 moveDirection = Vector3.zero;
	private CharacterController controller;
	private float gravity = 20f;

	// Use this for initialization
	void Start () {
		controller = GetComponent<CharacterController>();
		moveDirection.x = -10f;
	}
	
	// Update is called once per frame
	void Update () {
		if (controller.isGrounded) {
			if (Input.GetButton ("Jump")) {
				moveDirection.y = 10f;
			}
		}

		moveDirection.y -= gravity * Time.deltaTime;

		controller.Move (moveDirection * Time.deltaTime);
	}
}
