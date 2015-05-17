using UnityEngine;
using BlueRobe.Scene;

public class CharacterControl : MonoBehaviour
{
    public ActionStageSceneController sceneController;

    private static float MoveRatio = 0.05f;
    private static float MoveMax = 0.8f;
    private static float JumpPower = 100f;
    private static float ForwardSpeed = 0.5f;

    private Rigidbody rigidBody;
    private Animator animator;
    private bool isJumpCancelled;
    private bool isGrounded;
    private bool isWallTouched;
    private bool isFinished;
    private Vector3 move;

    // Use this for initialization
    void Start()
    {
        rigidBody = GetComponent<Rigidbody>();
        animator = GetComponent<Animator>();
    }

    // Update is called once per frame
    void FixedUpdate()
    {
        move = transform.position;
        if (!isFinished)
        {
            move += transform.forward * ForwardSpeed;
        }

        ProcessTouches();
        ProcessKeys();

        rigidBody.MovePosition(move);
    }

    void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("Ground"))
        {
            isGrounded = true;
        }
        else if (collision.gameObject.CompareTag("Wall"))
        {
            isWallTouched = true;
        }
    }

    void OnCollisionExit(Collision collision)
    {
        if (collision.gameObject.CompareTag("Ground"))
        {
            isGrounded = false;
        }
        else if (collision.gameObject.CompareTag("Wall"))
        {
            isWallTouched = false;
        }
    }

    void OnTriggerEnter(Collider collider)
    {
        if (collider.gameObject.CompareTag("Finish"))
        {
            StopMoving();
            sceneController.OnStageClear();
        }
        else if (collider.gameObject.CompareTag("Over"))
        {
            StopMoving();
            sceneController.OnGameOver();
        }
    }

    public void StartMoving()
    {
        animator.SetBool("isRunning", true);

        GameObject plane = GameObject.Find("Plane");
        GameObject runDust = GameObject.Find("RunDust");
        Renderer planeRenderer = plane.GetComponent("Renderer") as Renderer;
        Renderer dustRenderer = runDust.GetComponent("Renderer") as Renderer;
        dustRenderer.material.color = planeRenderer.material.color;

        ParticleSystem runDustParticleSystem = runDust.GetComponent("ParticleSystem") as ParticleSystem;
        runDustParticleSystem.Play();
    }

    public void StopMoving()
    {
        isFinished = true;
        animator.SetBool("isRunning", false);

        GameObject runDust = GameObject.Find("RunDust");
        ParticleSystem runDustParticleSystem = runDust.GetComponent("ParticleSystem") as ParticleSystem;
        runDustParticleSystem.Stop();
    }

    private void ProcessTouches()
    {
        if (Input.touchCount == 0)
        {
            return;
        }

        Touch touch = Input.GetTouch(0);
        if (touch.phase == TouchPhase.Began)
        {
            PrepareJump();
        }
        else if (touch.phase == TouchPhase.Moved)
        {
            Move(touch.deltaPosition.x);
        }
        else if (touch.phase == TouchPhase.Ended || touch.phase == TouchPhase.Canceled)
        {
            Jump();
        }
    }

    private void ProcessKeys()
    {
        if (Input.GetKeyDown("space"))
        {
            PrepareJump();
        }
        else if (Input.GetKeyUp("space"))
        {
            Jump();
        }

        float deltaX = Input.GetAxisRaw("Horizontal") * 5;
        if (deltaX != 0)
        {
            Move(deltaX);
        }
    }

    private void PrepareJump()
    {
        isJumpCancelled = false;
        AnimatorStateInfo stateInfo = animator.GetCurrentAnimatorStateInfo(0);
        if (isGrounded && stateInfo.IsName("Hikari.Run"))
        {
            animator.SetBool("PrepareJump", true);
        }
    }

    private void Move(float deltaX)
    {
        if (isWallTouched && transform.position.z * deltaX > 0)
        {
            return;
        }

        isJumpCancelled = true;
        AnimatorStateInfo stateInfo = animator.GetCurrentAnimatorStateInfo(0);
        if (deltaX != 0)
        {
            if (stateInfo.IsName("Hikari.PrepareJump"))
            {
                animator.SetBool("PrepareJump", false);
            }
            move += transform.right * Mathf.Clamp(deltaX * MoveRatio, -MoveMax, MoveMax);
        }
    }

    private void Jump()
    {
        animator.SetBool("PrepareJump", false);
        if (isGrounded && !isJumpCancelled)
        {
            rigidBody.AddForce(Vector3.up * JumpPower, ForceMode.Impulse);
        }
    }
}
