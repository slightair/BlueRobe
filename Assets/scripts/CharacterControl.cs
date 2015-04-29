using UnityEngine;
using System.Collections;

public class CharacterControl : MonoBehaviour {

	private Rigidbody rigidBody;

	// Use this for initialization
	void Start () {
		rigidBody = GetComponent<Rigidbody> ();
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetMouseButtonUp(0)) {
			if (transform.position.y < 0) {
				rigidBody.AddForce(Vector3.up * 5, ForceMode.VelocityChange);
			}
		}
	}
}
