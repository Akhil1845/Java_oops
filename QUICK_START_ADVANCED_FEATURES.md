# 🚀 Quick Start Guide - Advanced Features

## ⚠️ READ THIS FIRST

**Your existing project is SAFE.** All new features are **additions**, not modifications.

---

## 📋 Step-by-Step Implementation

### Step 1: Backup Everything (5 minutes)

```bash
# Backup your database
mysqldump -u root -p skill_bridge > skill_bridge_backup_$(date +%Y%m%d).sql

# Backup your code (if not using Git)
# Copy entire project folder to a safe location
```

### Step 2: Run Database Schema (10 minutes)

```sql
-- Open MySQL Workbench or Command Line
-- Run the file: ADVANCED_FEATURES_DATABASE_SCHEMA.sql

-- OR copy-paste this minimal version for Phase 1:

USE skill_bridge;

-- Skills table
CREATE TABLE IF NOT EXISTS skills (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Student Skills
CREATE TABLE IF NOT EXISTS student_skills (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency_level VARCHAR(20) DEFAULT 'beginner',
    verified BOOLEAN DEFAULT FALSE,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_skill (student_id, skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert sample skills
INSERT INTO skills (name, category, description) VALUES
('Java', 'technical', 'Object-oriented programming'),
('Python', 'technical', 'High-level programming'),
('JavaScript', 'technical', 'Web programming'),
('React', 'technical', 'Frontend library'),
('SQL', 'technical', 'Database queries'),
('Communication', 'soft', 'Effective communication'),
('Teamwork', 'soft', 'Collaboration'),
('Problem Solving', 'soft', 'Analytical thinking');

-- Verify
SELECT * FROM skills;
```

### Step 3: Add Entity Files (Already Created)

✅ Files created:
- `Skill.java`
- `StudentSkill.java`

### Step 4: Create Repositories (5 minutes)

Create these files in `repository` package:

**SkillRepository.java:**
```java
package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByName(String name);
    List<Skill> findByCategory(String category);
    List<Skill> findByNameContainingIgnoreCase(String name);
}
```

**StudentSkillRepository.java:**
```java
package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    List<StudentSkill> findByStudent_Id(Long studentId);
    boolean existsByStudent_IdAndSkill_Id(Long studentId, Long skillId);
}
```

### Step 5: Create Services (10 minutes)

**SkillService.java:**
```java
package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Skill;
import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.entity.StudentSkill;
import com.microinternship.skillbridge.repository.SkillRepository;
import com.microinternship.skillbridge.repository.StudentRepository;
import com.microinternship.skillbridge.repository.StudentSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private StudentSkillRepository studentSkillRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public List<Skill> searchSkills(String query) {
        return skillRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Skill> getSkillsByCategory(String category) {
        return skillRepository.findByCategory(category);
    }

    public StudentSkill addSkillToStudent(Long studentId, Long skillId, String proficiencyLevel) {
        if (studentSkillRepository.existsByStudent_IdAndSkill_Id(studentId, skillId)) {
            throw new RuntimeException("Skill already added");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        StudentSkill studentSkill = new StudentSkill();
        studentSkill.setStudent(student);
        studentSkill.setSkill(skill);
        studentSkill.setProficiencyLevel(proficiencyLevel);

        return studentSkillRepository.save(studentSkill);
    }

    public List<StudentSkill> getStudentSkills(Long studentId) {
        return studentSkillRepository.findByStudent_Id(studentId);
    }

    public void removeSkillFromStudent(Long studentSkillId) {
        studentSkillRepository.deleteById(studentSkillId);
    }
}
```

### Step 6: Create Controllers (10 minutes)

**SkillController.java:**
```java
package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.Skill;
import com.microinternship.skillbridge.entity.StudentSkill;
import com.microinternship.skillbridge.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    @GetMapping("/search")
    public List<Skill> searchSkills(@RequestParam String query) {
        return skillService.searchSkills(query);
    }

    @GetMapping("/category/{category}")
    public List<Skill> getSkillsByCategory(@PathVariable String category) {
        return skillService.getSkillsByCategory(category);
    }

    @PostMapping("/student")
    public ResponseEntity<?> addSkillToStudent(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            Long skillId = Long.valueOf(request.get("skillId").toString());
            String proficiency = request.getOrDefault("proficiencyLevel", "beginner").toString();

            StudentSkill result = skillService.addSkillToStudent(studentId, skillId, proficiency);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public List<StudentSkill> getStudentSkills(@PathVariable Long studentId) {
        return skillService.getStudentSkills(studentId);
    }

    @DeleteMapping("/student/{studentSkillId}")
    public ResponseEntity<Void> removeSkillFromStudent(@PathVariable Long studentSkillId) {
        skillService.removeSkillFromStudent(studentSkillId);
        return ResponseEntity.ok().build();
    }
}
```

### Step 7: Test Backend (15 minutes)

1. **Restart Spring Boot Application**
2. **Test with Postman or Browser:**

```bash
# Get all skills
GET http://localhost:8080/api/skills

# Search skills
GET http://localhost:8080/api/skills/search?query=java

# Add skill to student
POST http://localhost:8080/api/skills/student
Body: {
  "studentId": 1,
  "skillId": 1,
  "proficiencyLevel": "intermediate"
}

# Get student skills
GET http://localhost:8080/api/skills/student/1
```

### Step 8: Add Frontend (30 minutes)

Add this to `student_dashboard.html` in the profile section:

```html
<!-- Add after profile details -->
<div class="skills-section">
  <h3>My Skills</h3>
  <div id="studentSkills"></div>
  <button onclick="showAddSkillModal()">+ Add Skill</button>
</div>

<!-- Add Skill Modal -->
<div id="addSkillModal" style="display:none;">
  <div class="modal-content">
    <h3>Add Skill</h3>
    <input type="text" id="skillSearch" placeholder="Search skills..." onkeyup="searchSkills()">
    <div id="skillResults"></div>
    <select id="proficiencyLevel">
      <option value="beginner">Beginner</option>
      <option value="intermediate">Intermediate</option>
      <option value="advanced">Advanced</option>
      <option value="expert">Expert</option>
    </select>
    <button onclick="addSkill()">Add</button>
    <button onclick="closeAddSkillModal()">Cancel</button>
  </div>
</div>

<script>
let selectedSkillId = null;

async function loadStudentSkills() {
  const studentId = localStorage.getItem('studentId');
  const res = await fetch(`http://localhost:8080/api/skills/student/${studentId}`);
  const skills = await res.json();
  
  const container = document.getElementById('studentSkills');
  container.innerHTML = skills.map(s => `
    <div class="skill-tag">
      ${s.skill.name} - ${s.proficiencyLevel}
      <button onclick="removeSkill(${s.id})">×</button>
    </div>
  `).join('');
}

async function searchSkills() {
  const query = document.getElementById('skillSearch').value;
  if (query.length < 2) return;
  
  const res = await fetch(`http://localhost:8080/api/skills/search?query=${query}`);
  const skills = await res.json();
  
  document.getElementById('skillResults').innerHTML = skills.map(s => `
    <div class="skill-option" onclick="selectSkill(${s.id}, '${s.name}')">
      ${s.name} (${s.category})
    </div>
  `).join('');
}

function selectSkill(id, name) {
  selectedSkillId = id;
  document.getElementById('skillSearch').value = name;
  document.getElementById('skillResults').innerHTML = '';
}

async function addSkill() {
  const studentId = localStorage.getItem('studentId');
  const proficiency = document.getElementById('proficiencyLevel').value;
  
  const res = await fetch('http://localhost:8080/api/skills/student', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({
      studentId: studentId,
      skillId: selectedSkillId,
      proficiencyLevel: proficiency
    })
  });
  
  if (res.ok) {
    alert('Skill added!');
    closeAddSkillModal();
    loadStudentSkills();
  }
}

async function removeSkill(id) {
  if (!confirm('Remove this skill?')) return;
  
  await fetch(`http://localhost:8080/api/skills/student/${id}`, {method: 'DELETE'});
  loadStudentSkills();
}

function showAddSkillModal() {
  document.getElementById('addSkillModal').style.display = 'flex';
}

function closeAddSkillModal() {
  document.getElementById('addSkillModal').style.display = 'none';
  selectedSkillId = null;
}

// Load skills when profile is shown
// Add to your existing navigate() function
if (section === 'profile') {
  loadProfile();
  loadStudentSkills();
}
</script>

<style>
.skills-section {
  margin-top: 20px;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 8px;
}

.skill-tag {
  display: inline-block;
  background: #1e90ff;
  color: white;
  padding: 8px 12px;
  border-radius: 20px;
  margin: 5px;
}

.skill-tag button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 18px;
  margin-left: 8px;
}

#addSkillModal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 3000;
}

.skill-option {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #ddd;
}

.skill-option:hover {
  background: #f0f0f0;
}
</style>
```

### Step 9: Test Everything (10 minutes)

✅ **Checklist:**
- [ ] Old features still work (login, internships, tests)
- [ ] Can view all skills
- [ ] Can search skills
- [ ] Can add skill to student profile
- [ ] Can remove skill from profile
- [ ] Skills display correctly
- [ ] No console errors

---

## 🎉 Congratulations!

You've successfully implemented **Phase 1: Skills System**!

### What You Now Have:
✅ Skills database with sample data
✅ Student can add/remove skills
✅ Proficiency levels
✅ Skill search functionality
✅ Skills display on profile

### Next Steps:
1. **Test thoroughly** for a few days
2. **Get user feedback**
3. **Move to Phase 2** (Gamification) when ready

---

## 🆘 Troubleshooting

### Problem: Application won't start
**Solution:** Check console for errors, verify database connection

### Problem: Skills not showing
**Solution:** Check browser console, verify API endpoints work

### Problem: Can't add skills
**Solution:** Verify studentId is correct, check database foreign keys

### Problem: Old features broken
**Solution:** Rollback database, restore code backup, start over

---

## 📞 Need Help?

1. Check error messages carefully
2. Google the error
3. Check Stack Overflow
4. Review the code files created

---

## 🎯 Success Criteria

You're ready for Phase 2 when:
- ✅ All old features work
- ✅ Skills system works perfectly
- ✅ No bugs for 3 days
- ✅ Users are happy with skills feature

**Don't rush! Quality > Speed**

---

Remember: **One feature at a time, tested thoroughly!**
