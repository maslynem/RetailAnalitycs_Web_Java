package ru.school.retailanalitycs_web_java.controllers.viewControllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.school.retailanalitycs_web_java.dto.viewDto.GroupViewDto;
import ru.school.retailanalitycs_web_java.mapper.GroupViewMapper;
import ru.school.retailanalitycs_web_java.services.viewServices.GroupViewService;

import java.util.List;

@RestController
@RequestMapping("api/v1/views/groups")
public class GroupViewController {
    private final GroupViewService groupViewService;
    private final GroupViewMapper groupViewMapper;

    public GroupViewController(GroupViewService groupViewService, GroupViewMapper groupViewMapper) {
        this.groupViewService = groupViewService;
        this.groupViewMapper = groupViewMapper;
    }

    @GetMapping
    public List<GroupViewDto> findAllGroups() {
        return groupViewService.findAll().stream().map(groupViewMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<GroupViewDto> findAllGroupsByPage(@RequestParam("page") int page,
                                                  @RequestParam("size") int size) {
        return groupViewService.findAllByPage(page, size).map(groupViewMapper::toDto);
    }

//    @GetMapping("/{customerId}/{skuGroupId}")
//    public GroupViewDto findGroupById(@PathVariable Long customerId,
//                                      @PathVariable Long skuGroupId) {
//        GroupViewId id = new GroupViewId(customerId, skuGroupId);
//        return groupViewService.findById(id).map(groupViewMapper::toDto).orElseThrow(() -> new GroupViewNotFoundException(id));
//    }

}
